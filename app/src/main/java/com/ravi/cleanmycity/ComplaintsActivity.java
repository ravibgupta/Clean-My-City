package com.ravi.cleanmycity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ravi.cleanmycity.adapter.ComplaintAdapter;
import com.ravi.cleanmycity.helper.ConnectionDetector;
import com.ravi.cleanmycity.helper.SessionManager;

public class ComplaintsActivity extends AppCompatActivity {

    public List<com.ravi.cleanmycity.adapter.Complaint> complaintList = new ArrayList<>();
    ProgressBar pBar;
    ComplaintAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;
    LinearLayout noInternetLayout;
    RecyclerView recyclerView;

    @IgnoreExtraProperties
    public static class Complaint {

        public String email;
        public String address;
        public String image_path;
        public String date;
        public String detail;

        public Complaint() {

        }

        public String getEmail() {
            return email;
        }

        public String getAddress() {
            return address;
        }

        public String getImage_path() {
            return image_path;
        }

        public String getDate() {
            return date;
        }

        public String getDetail() {
            return detail;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noInternetLayout = (LinearLayout) findViewById(R.id.noInternetLayout);

        loadComplaints();

        mAdapter = new ComplaintAdapter(ComplaintsActivity.this, complaintList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                com.ravi.cleanmycity.adapter.Complaint c = complaintList.get(position);
                Intent i = new Intent(getApplicationContext(), ViewComplaint.class);
                i.putExtra("URL", c.getUrl());
                i.putExtra("DETAIL", c.getDetail());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    public void loadComplaints() {

        complaintList.clear();

        SessionManager session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        ConnectionDetector cd = new ConnectionDetector(this);
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("complaints/" + user.get(SessionManager.KEY_ID));

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Complaint complaint = snapshot.getValue(Complaint.class);
                        com.ravi.cleanmycity.adapter.Complaint c = new com.ravi.cleanmycity.adapter.Complaint(complaint.getAddress(), snapshot.getKey(), complaint.getDate(), complaint.getDetail(), complaint.getImage_path());
                        complaintList.add(c);
                    }
                    if (complaintList.isEmpty()) {
                        noInternetLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        noInternetLayout.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    }
                    pBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        } else {
            pBar.setVisibility(View.INVISIBLE);
            noInternetLayout.setVisibility(View.VISIBLE);
            Toast.makeText(ComplaintsActivity.this, "No internet connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.complaints, menu);
        return super.onCreateOptionsMenu(menu);

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
        } else if (id == R.id.action_refresh) {
            noInternetLayout.setVisibility(View.GONE);
            pBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            loadComplaints();
        }

        return super.onOptionsItemSelected(item);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
