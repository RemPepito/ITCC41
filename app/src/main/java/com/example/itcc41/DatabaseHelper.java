package com.example.itcc41;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper {

    private static final String DATABASE_NAME = "imaginary.db";
    private static final String DATABASE_PATH = "/data/data/com.example.itcc41/databases/";

    public static void initializeDatabase(Context context) {
        File databaseFile = new File(DATABASE_PATH + DATABASE_NAME);

        // Check if the database file already exists
        if (!databaseFile.exists()) {
            try {
                // Create the databases directory if it doesn't exist
                File databaseDir = new File(DATABASE_PATH);
                if (!databaseDir.exists()) {
                    databaseDir.mkdirs();
                }

                // Open the file from the raw folder
                InputStream inputStream = context.getResources().openRawResource(R.raw.imaginary); // Update to match your file name
                OutputStream outputStream = new FileOutputStream(databaseFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
