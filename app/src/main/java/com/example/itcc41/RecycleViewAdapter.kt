package com.example.itcc41

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class RecyclerViewAdapter(
    private val context: Context,
    private val dataList: List<DataModel>,
    private val isGif: Boolean
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uploaderName: TextView = itemView.findViewById(R.id.uploaderName)
        val uploaderID: TextView = itemView.findViewById(R.id.uploadID) // Added for uploader ID
        val uploaderImage: ImageView = itemView.findViewById(R.id.uploaderImage)
        val downloadButton: Button = itemView.findViewById(R.id.downloadButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        // Set uploader name and ID dynamically
        holder.uploaderName.text = "USER: ${item.uploader}"
        holder.uploaderID.text = "ID: ${item.id}" // Display uploader ID

        if (isGif) {
            // Display GIF using Glide
            Glide.with(context)
                .asGif()
                .load(item.image) // Byte array for the GIF
                .into(holder.uploaderImage)
        } else {
            // Display image
            holder.uploaderImage.setImageBitmap(
                BitmapFactory.decodeByteArray(item.image, 0, item.image.size)
            )
        }

        holder.uploaderImage.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("ITEM_ID", item.id)
                putExtra("UPLOADER", item.uploader)
                putExtra("SEARCH", item.search)
                putExtra("CATEGORY", item.category)
                putExtra("LIKES", item.likes)
                putExtra("DOWNLOADS", item.downloads)
                putExtra("COMMENTS", item.comments)
                putExtra("IMAGE", item.image) // Pass the image byte array
            }
        }


        // Handle download button click
        holder.downloadButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("ITEM_ID", item.id)
                putExtra("UPLOADER", item.uploader)
                putExtra("SEARCH", item.search)
                putExtra("CATEGORY", item.category)
                putExtra("LIKES", item.likes)
                putExtra("DOWNLOADS", item.downloads)
                putExtra("COMMENTS", item.comments)
                putExtra("IMAGE", item.image) // Pass the image byte array
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun saveToDevice(data: ByteArray, extension: String) {
        val fileName = "downloaded_${System.currentTimeMillis()}.$extension"
        val file = File(context.getExternalFilesDir(null), fileName)
        try {
            file.writeBytes(data)
            Toast.makeText(context, "Saved to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error saving file", Toast.LENGTH_SHORT).show()
            Log.e("SaveToDevice", "Error saving file", e)
        }
    }
}

// Data model for RecyclerView items
class DataModel(
    val id: Int, // Added uploader ID
    val uploader: String,
    val image: ByteArray,
    val search: String,
    val category: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int
)
