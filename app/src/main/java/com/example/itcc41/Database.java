package com.example.itcc41;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class Database extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "imaginary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "imaginary";
    private static final String COLUMN_ID = "_id";
    private static final String AUTHOR = "uploader";
    private static final String LIKES = "likes";
    private static final String DOWNLOADS = "downloads";
    private static final String COMMENTS = "comments";
    private static final String SEARCH = "search";
    private static final String IMAGE = "image";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AUTHOR + " TEXT, " +
                        SEARCH + " TEXT, " +
                        LIKES + " INTEGER, " +
                        DOWNLOADS + " INTEGER, " +
                        COMMENTS + " INTEGER, " +
                        IMAGE + " BLOB);"; // Add BLOB column for image
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert a record with an image
    public void addRecord(String author, String search, int likes, int downloads, int comments, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AUTHOR, author);
        values.put(SEARCH, search);
        values.put(LIKES, likes);
        values.put(DOWNLOADS, downloads);
        values.put(COMMENTS, comments);
        values.put(IMAGE, imageToByteArray(image)); // Convert image to byte array
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Method to convert Bitmap to byte array
    private byte[] imageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Compress image as PNG
        return stream.toByteArray();
    }

    // Method to retrieve an image by ID
    public Bitmap getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + IMAGE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            try {
                // Debug: Log column names
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    Log.d("DB_DEBUG", "Column: " + columnName);
                }

                // Fetch the image blob
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE));
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error retrieving image", e);
            } finally {
                cursor.close();
            }
        }
        cursor.close();
        return null;
    }

}
