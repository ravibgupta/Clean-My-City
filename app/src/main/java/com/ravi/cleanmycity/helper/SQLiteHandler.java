package com.ravi.cleanmycity.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pending";

    // Login table name
    private static final String TABLE_COMPLAINTS = "complaints";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_DATE = "date";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_COMPLAINTS + "("
                + KEY_ID + " TEXT," + KEY_IMAGE + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_EMAIL + " TEXT," + KEY_DETAIL + " TEXT," + KEY_DATE + " TEXT" +")";
        db.execSQL(CREATE_IMAGE_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addImage(String id, String image, String latitude, String longitude, String address, String email, String detail, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_IMAGE, image);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_EMAIL, email);
        values.put(KEY_DETAIL, detail);
        values.put(KEY_DATE, date);

        // Inserting Row
        long idd = db.insert(TABLE_COMPLAINTS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New image inserted into sqlite: " + idd);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getImageDetails(int position) {
        HashMap<String, String> image = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.move(position + 1);
        if (cursor.getCount() > 0) {
            image.put("id", cursor.getString(0));
            image.put("image", cursor.getString(1));
            image.put("latitude", cursor.getString(2));
            image.put("longitude", cursor.getString(3));
            image.put("address", cursor.getString(4));
            image.put("email", cursor.getString(5));
            image.put("detail", cursor.getString(6));
            image.put("date", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching branch from Sqlite: " + image.toString());

        return image;
    }

    public int getTableSize() {
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAINTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.getCount();
    }

    public void deleteImage(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete(TABLE_COMPLAINTS, "id = ?", new String[] { id });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteBranch() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_COMPLAINTS, null, null);
        db.close();

        Log.d(TAG, "Deleted all imag info from sqlite");
    }

}