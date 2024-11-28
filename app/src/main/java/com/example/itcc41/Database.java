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

    private static final String DATABASE_NAME = "imaginary.db";
    private static final int DATABASE_VERSION = 3;

    // Table for images
    public static final String TABLE_NAME = "imaginary";
    private static final String COLUMN_ID = "_id";
    private static final String AUTHOR = "uploader";
    private static final String SEARCH = "search";
    private static final String CATEGORY = "category";
    private static final String LIKES = "likes";
    private static final String DOWNLOADS = "downloads";
    private static final String COMMENTS = "comments";
    private static final String IMAGE = "image";

    // Table for GIFs
    public static final String GIF_TABLE_NAME = "gifs";
    private static final String GIF_COLUMN_ID = "id";
    private static final String GIF_AUTHOR = "uploader";
    private static final String GIF_SEARCH = "search";
    private static final String GIF_CATEGORY = "category";
    private static final String GIF_LIKES = "likes";
    private static final String GIF_DOWNLOADS = "downloads";
    private static final String GIF_COMMENTS = "comments";
    private static final String GIF_DATA = "gif";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the images table
        String createImageTableQuery =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AUTHOR + " TEXT, " +
                        SEARCH + " TEXT, " +
                        CATEGORY + " TEXT, " +
                        LIKES + " INTEGER, " +
                        DOWNLOADS + " INTEGER, " +
                        COMMENTS + " INTEGER, " +
                        IMAGE + " BLOB);";
        db.execSQL(createImageTableQuery);

        // Create the GIFs table
        String createGifTableQuery =
                "CREATE TABLE " + GIF_TABLE_NAME +
                        " (" + GIF_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GIF_AUTHOR + " TEXT, " +
                        GIF_SEARCH + " TEXT, " +
                        GIF_CATEGORY + " TEXT, " +
                        GIF_LIKES + " INTEGER, " +
                        GIF_DOWNLOADS + " INTEGER, " +
                        GIF_COMMENTS + " INTEGER, " +
                        GIF_DATA + " BLOB);";
        db.execSQL(createGifTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GIF_TABLE_NAME);
        onCreate(db);
    }

    // Generalized method to fetch details by ID
    public DataModel getDetailsById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // First, check in the images table
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            DataModel dataModel = new DataModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SEARCH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(LIKES)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DOWNLOADS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COMMENTS))
            );
            cursor.close();
            return dataModel;
        }
        if (cursor != null) cursor.close();

        // If not found in images table, check in the GIFs table
        cursor = db.rawQuery("SELECT * FROM " + GIF_TABLE_NAME + " WHERE " + GIF_COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            DataModel dataModel = new DataModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(GIF_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(GIF_AUTHOR)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(GIF_DATA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(GIF_SEARCH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(GIF_CATEGORY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(GIF_LIKES)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(GIF_DOWNLOADS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(GIF_COMMENTS))
            );
            cursor.close();
            return dataModel;
        }
        if (cursor != null) cursor.close();

        return null; // If not found in either table
    }

    // DataModel class
    public class DataModel {
        private int id;
        private String uploader;
        private byte[] data; // Can be an image or a GIF
        private String search;
        private String category;
        private int likes;
        private int downloads;
        private int comments;

        public DataModel(int id, String uploader, byte[] data, String search, String category, int likes, int downloads, int comments) {
            this.id = id;
            this.uploader = uploader;
            this.data = data;
            this.search = search;
            this.category = category;
            this.likes = likes;
            this.downloads = downloads;
            this.comments = comments;
        }

        public int getId() {
            return id;
        }

        public String getUploader() {
            return uploader;
        }

        public byte[] getData() {
            return data;
        }

        public String getSearch() {
            return search;
        }

        public String getCategory() {
            return category;
        }

        public int getLikes() {
            return likes;
        }

        public int getDownloads() {
            return downloads;
        }

        public int getComments() {
            return comments;
        }
    }
}


