package com.ravi.cleanmycity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ravi.cleanmycity.adapter.Pending;
import com.ravi.cleanmycity.adapter.PendingAdapter;
import com.ravi.cleanmycity.helper.SQLiteHandler;

public class PendingActivity extends AppCompatActivity {

    private SQLiteHandler db;
    public List<Pending> pendingList = new ArrayList<>();
    ProgressBar pBar;
    PendingAdapter mAdapter;
    HashMap<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new SQLiteHandler(getApplicationContext());

        pBar = (ProgressBar) findViewById(R.id.progressBar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayout noInternetLayout = (LinearLayout) findViewById(R.id.noInternetLayout);

        if (db.getTableSize() == 0) {
            pBar.setVisibility(View.INVISIBLE);
            noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < db.getTableSize(); i++) {
                HashMap<String, String> image = db.getImageDetails(i);

                String address = image.get("latitude") + " , " + image.get("longitude");
                String time = image.get("date");
                String id = image.get("id");
                String path = image.get("image");
                Pending p = new Pending(path, address, id, time);

                pendingList.add(p);
            }

            pBar.setVisibility(View.INVISIBLE);
            db.close();
        }

        mAdapter = new PendingAdapter(PendingActivity.this, pendingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                data = db.getImageDetails(position);
                Intent i = new Intent(PendingActivity.this, ImageCaptureActivity.class);
                i.putExtra("Latitude", data.get("latitude"));
                i.putExtra("Longitude", data.get("longitude"));
                i.putExtra("RealPath", data.get("image"));
                i.putExtra("Detail", data.get("detail"));
                i.putExtra("Type", "pending");
                i.putExtra("ID", data.get("id"));
                startActivity(i);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
