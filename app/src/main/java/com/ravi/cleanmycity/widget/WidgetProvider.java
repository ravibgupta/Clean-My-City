package com.ravi.cleanmycity.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ravi.cleanmycity.MainActivity;
import com.ravi.cleanmycity.R;
import com.ravi.cleanmycity.helper.SessionManager;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        SessionManager session = new SessionManager(context.getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("WIDGET", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            remoteViews.setOnClickPendingIntent(R.id.postComplaint, pendingIntent);
        }

        pushWidgetUpdate(context, remoteViews);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context,
                WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}