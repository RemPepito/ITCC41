package com.example.itcc41

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.download_layout)

        val database = Database(this)
        val itemId = intent.getIntExtra("ITEM_ID", -1) // Pass the item ID through Intent

        val dataModel = database.getDetailsById(itemId)

        if (dataModel != null) {
            findViewById<TextView>(R.id.username).text = "Uploader: ${dataModel.uploader}"
            findViewById<TextView>(R.id.uploadID).text = "ID: ${dataModel.id}"
            findViewById<TextView>(R.id.likesCount).text = dataModel.likes.toString()
            findViewById<TextView>(R.id.downloadsCount).text = dataModel.downloads.toString()
            findViewById<TextView>(R.id.commentsCount).text = dataModel.comments.toString()

            val selectedImage = findViewById<ImageView>(R.id.selected_image)

            if (dataModel.category == "gif") {
                database.incrementViewsById(dataModel.id, "gifs")
                // Load GIF using Glide
                Glide.with(this)
                    .asGif()
                    .load(dataModel.data)
                    .into(selectedImage)
            } else {
                // Load image as a Bitmap
                database.incrementViewsById(dataModel.id, "imaginary")
                val bitmap = BitmapFactory.decodeByteArray(dataModel.data, 0, dataModel.data.size)
                selectedImage.setImageBitmap(bitmap)
            }

            // Handle download button click
            findViewById<Button>(R.id.downloadActionButton).setOnClickListener {
                saveToGallery(dataModel)
            }
        }

        // Handle back button
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }
    }

    private fun saveToGallery(dataModel: Database.DataModel) {
        val database = Database(this)
        // Get the Downloads directory
        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (!downloadsFolder.exists()) downloadsFolder.mkdirs() // Create the folder if it doesn't exist

        val fileName = if (dataModel.category == "gif") "gif_${dataModel.id}.gif" else "image_${dataModel.id}.jpg"
        val file = File(downloadsFolder, fileName)

        try {
            FileOutputStream(file).use { fos ->
                if (dataModel.category == "gif") {
                    database.incrementDownloadsById(dataModel.id, "gifs")
                    fos.write(dataModel.data) // Save GIF data directly
                } else {
                    database.incrementDownloadsById(dataModel.id, "imaginary")
                    val bitmap = BitmapFactory.decodeByteArray(dataModel.data, 0, dataModel.data.size)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos) // Compress and save image
                }
            }

            // Notify the media scanner about the new file
            val uri = Uri.fromFile(file)
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

            Toast.makeText(this, "Saved to Downloads: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to save file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}
