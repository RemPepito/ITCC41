package com.example.itcc41

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private val context: Context,
    private val dataList: List<DataModel>
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uploaderName: TextView = itemView.findViewById(R.id.uploaderName)
        val uploaderImage: ImageView = itemView.findViewById(R.id.uploaderImage)
        val downloadButton: Button = itemView.findViewById(R.id.downloadButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        // Set uploader name dynamically
        holder.uploaderName.text = item.uploader
        // Decode the image BLOB and set it to the ImageView
        holder.uploaderImage.setImageBitmap(BitmapFactory.decodeByteArray(item.image, 0, item.image.size))
        holder.downloadButton.setOnClickListener {
            // Handle download action here (if needed)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

// Data model for RecyclerView items
data class DataModel(
    val uploader: String, // Uploader name
    val image: ByteArray  // Image as BLOB
)
