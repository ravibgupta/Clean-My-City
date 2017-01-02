package com.ravi.cleanmycity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        String success = getIntent().getStringExtra("SUCCESS");
        String key = getIntent().getStringExtra("KEY");

        switch (success) {
            case "success": {
                Dialog dialog = new Dialog(FinalActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_success);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView complaintID = (TextView) dialog.findViewById(R.id.complaintId);
                complaintID.setText(key);
                Button viewPending = (Button) dialog.findViewById(R.id.viewPending);
                viewPending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), PendingActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                Button postComplaint = (Button) dialog.findViewById(R.id.postComplaint);
                postComplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                dialog.show();
                break;
            }
            case "fail": {
                final Dialog dialog = new Dialog(FinalActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_nointernet);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Button viewPending = (Button) dialog.findViewById(R.id.viewPending);
                viewPending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), PendingActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                Button postComplaint = (Button) dialog.findViewById(R.id.postComplaint);
                postComplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                dialog.show();

                break;
            }
            case "sorry": {
                Dialog dialog = new Dialog(FinalActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_sorry);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Button viewPending = (Button) dialog.findViewById(R.id.viewPending);
                viewPending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                Button postComplaint = (Button) dialog.findViewById(R.id.postComplaint);
                postComplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                dialog.show();
                break;
            }
        }

    }
}
