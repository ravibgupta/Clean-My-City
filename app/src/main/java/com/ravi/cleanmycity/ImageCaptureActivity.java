package com.ravi.cleanmycity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.ravi.cleanmycity.helper.ConnectionDetector;
import com.ravi.cleanmycity.helper.RequestHandler;
import com.ravi.cleanmycity.helper.SQLiteHandler;
import com.ravi.cleanmycity.helper.SessionManager;

public class ImageCaptureActivity extends AppCompatActivity {

    String latitude, longitude, address, email, realPath, detail, type;
    ProgressDialog loading;
    String url, id;
    EditText details;
    ImageView capturedImage;
    private DatabaseReference mDatabase;
    SessionManager session;
    StorageReference storageRef;

    @IgnoreExtraProperties
    public class Complaint {

        public String email;
        public String address;
        public String image_path;
        public String date;
        public String detail;
        public Map<String, Boolean> stars = new HashMap<>();

        public Complaint() {
            // Default constructor required for calls to DataSnapshot.getValue(Complaint.class)
        }

        public Complaint(String email, String address, String image_path, String date, String detail) {
            this.email = email;
            this.address = address;
            this.image_path = image_path;
            this.date = date;
            this.detail = detail;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("email", email);
            result.put("address", address);
            result.put("image_path", image_path);
            result.put("date", date);
            result.put("detail", detail);
            result.put("stars", stars);

            return result;
        }

    }

    private void writeNewComplaint(final String name, final String userId, final String email, final String address, final String date, final String detail) {

        final String key = mDatabase.child("complaints").push().getKey();

        final StorageReference complaintImageRef = storageRef.child("images/" + key + ".jpg");

        capturedImage.setDrawingCacheEnabled(true);
        capturedImage.buildDrawingCache();
        Bitmap bitmap = capturedImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = complaintImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                loading.dismiss();
                Intent i = new Intent(ImageCaptureActivity.this, FinalActivity.class);
                i.putExtra("SUCCESS", "sorry");
                startActivity(i);
                finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                url = downloadUrl.toString();

                Complaint complaint = new Complaint(email, address, url, date, detail);
                Map<String, Object> complaintValues = complaint.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/complaints/" + userId + "/" + key, complaintValues);

                mDatabase.updateChildren(childUpdates);

                if (type.equals("pending")) {
                    SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                    db.deleteImage(id);
                }

                loading.dismiss();
                Intent i = new Intent(ImageCaptureActivity.this, FinalActivity.class);
                i.putExtra("KEY", key);
                i.putExtra("SUCCESS", "success");
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_caputre);


        latitude = getIntent().getStringExtra("Latitude");
        longitude = getIntent().getStringExtra("Longitude");
        realPath = getIntent().getStringExtra("RealPath");
        type = getIntent().getStringExtra("Type");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session = new SessionManager(getApplicationContext());

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        storageRef = storage.getReferenceFromUrl("gs://clean-city-378a7.appspot.com");

        details = (EditText) findViewById(R.id.input_details);

        capturedImage = (ImageView) findViewById(R.id.capturedImage);

        if (type.equals("pending")) {
            id = getIntent().getStringExtra("ID");
            details.setText(getIntent().getStringExtra("Detail"));
            details.setEnabled(false);
        }

        File file = new File(realPath);

        Glide.with(this)
                .load(file)
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(capturedImage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{Uri.parse("file://" + Environment.getExternalStorageDirectory()).toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    // TODO Auto-generated method stub

                }
            });
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectionDetector cd = new ConnectionDetector(ImageCaptureActivity.this);
                Boolean isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    loading = ProgressDialog.show(ImageCaptureActivity.this, null, "Posting Complaint...", true, true);
                    loading.setCancelable(false);

                    detail = details.getText().toString();

                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    new UploadImage().execute();

                } else {

                    if (type.equals("pending")) {
                        Toast.makeText(ImageCaptureActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    } else {

                        loading = ProgressDialog.show(ImageCaptureActivity.this, null, "Saving Complaint...", true, true);
                        loading.setCancelable(false);

                        detail = details.getText().toString();

                        String uploadImage = realPath;
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                        db.addImage(String.valueOf(db.getTableSize() + 1), uploadImage, latitude, longitude, address, email, detail, formattedDate);
                        db.close();

                        loading.dismiss();

                        Intent i = new Intent(ImageCaptureActivity.this, FinalActivity.class);
                        i.putExtra("SUCCESS", "fail");
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {
                                          if (type.equals("pending")) {
                                              startActivity(new Intent(ImageCaptureActivity.this, PendingActivity.class));
                                          } else {
                                              onBackPressed();
                                          }
                                      }
                                  }

        );
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

    private class UploadImage extends AsyncTask<Bitmap, Void, String> {

        String JSON_STRING;
        JSONObject jsonObject = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Bitmap... params) {

            RequestHandler rh = new RequestHandler();
            JSON_STRING = rh.sendGetRequest("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude);
            try {
                jsonObject = new JSONObject(JSON_STRING);
                JSONArray result = jsonObject.getJSONArray("results");
                JSONObject jo = result.getJSONObject(0);
                address = jo.getString("formatted_address");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String formattedDate = df.format(c.getTime());

            HashMap<String, String> user = session.getUserDetails();

            writeNewComplaint(user.get(SessionManager.KEY_NAME), user.get(SessionManager.KEY_ID), user.get(SessionManager.KEY_EMAIL), address, formattedDate, detail);

            return null;

        }
    }
}

