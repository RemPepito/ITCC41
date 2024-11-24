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

    // Table for images
    public static final String TABLE_IMAGE = "imaginary";
    private static final String COLUMN_ID = "_id";
    private static final String AUTHOR = "uploader";
    private static final String LIKES = "likes";
    private static final String DOWNLOADS = "downloads";
    private static final String COMMENTS = "comments";
    private static final String SEARCH = "search";
    private static final String CATEGORY = "category";
    private static final String IMAGE = "image";

    // Table for GIFs
    public static final String TABLE_GIF = "imaginary_gif";
    private static final String GIF = "gif"; // BLOB column for GIF

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for images
        String createImageTable =
                "CREATE TABLE " + TABLE_IMAGE +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AUTHOR + " TEXT, " +
                        SEARCH + " TEXT, " +
                        CATEGORY + " TEXT, " +
                        LIKES + " INTEGER, " +
                        DOWNLOADS + " INTEGER, " +
                        COMMENTS + " INTEGER, " +
                        IMAGE + " BLOB);"; // Add BLOB column for image

        // Create table for GIFs
        String createGifTable =
                "CREATE TABLE " + TABLE_GIF +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AUTHOR + " TEXT, " +
                        SEARCH + " TEXT, " +
                        CATEGORY + " TEXT, " +
                        LIKES + " INTEGER, " +
                        DOWNLOADS + " INTEGER, " +
                        COMMENTS + " INTEGER, " +
                        GIF + " BLOB);"; // Add BLOB column for GIF

        db.execSQL(createImageTable);
        db.execSQL(createGifTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GIF);
        onCreate(db);
    }

    // Method to insert a record with a GIF
    public void addGifRecord(String author, String search, String category, int likes, int downloads, int comments, byte[] gifData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AUTHOR, author);
        values.put(SEARCH, search);
        values.put(CATEGORY, category);
        values.put(LIKES, likes);
        values.put(DOWNLOADS, downloads);
        values.put(COMMENTS, comments);
        values.put(GIF, gifData); // Insert GIF as byte array
        db.insert(TABLE_GIF, null, values);
        db.close();
    }

    // Method to retrieve a GIF by ID
    public byte[] getGif(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + GIF + " FROM " + TABLE_GIF + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            try {
                return cursor.getBlob(cursor.getColumnIndexOrThrow(GIF));
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error retrieving GIF", e);
            } finally {
                cursor.close();
            }
        }
        cursor.close();
        return null;
    }
}



