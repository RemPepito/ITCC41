package com.example.itcc41

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PageActivity : AppCompatActivity() {
    private lateinit var photosTab: TextView
    private lateinit var illustrationsTab: TextView
    private lateinit var videosTab: TextView
    private lateinit var gifsTab: TextView

    private lateinit var photosUnderline: View
    private lateinit var illustrationsUnderline: View
    private lateinit var videosUnderline: View
    private lateinit var gifsUnderline: View
    private lateinit var searchBar: EditText
    private lateinit var headerSlogan: TextView

    private var tabnumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Database(this)

        // Add multiple GIF records
        addSampleGifRecords(database)

        // Initialize views
        photosTab = findViewById(R.id.photosTab)
        illustrationsTab = findViewById(R.id.illustrationsTab)
        videosTab = findViewById(R.id.videosTab)
        gifsTab = findViewById(R.id.gifsTab)

        photosUnderline = findViewById(R.id.photosUnderline)
        illustrationsUnderline = findViewById(R.id.illustrationsUnderline)
        videosUnderline = findViewById(R.id.videosUnderline)
        gifsUnderline = findViewById(R.id.gifsUnderline)
        searchBar = findViewById(R.id.searchMainBar)
        headerSlogan = findViewById(R.id.results_text)

        setupTabs(database)

        searchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            ) {
                val searchText = searchBar.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    headerSlogan.text = "Results for \"$searchText\""
                }
                when (tabnumber) {
                    1 -> performSearch(database, "photo", searchText)
                    2 -> performSearch(database, "illustration", searchText)
                    4 -> performSearch(database, "gif", searchText)
                }
                true // Indicates the event was handled
            } else {
                false
            }
        }
    }

    private fun setupTabs(database: Database) {
        fun resetTabs() {
            val tabs = listOf(photosTab, illustrationsTab, videosTab, gifsTab)
            val underlines = listOf(photosUnderline, illustrationsUnderline, videosUnderline, gifsUnderline)
            for (tab in tabs) {
                tab.setTextColor(ContextCompat.getColor(this, R.color.black)) // Reset text color to black
                tab.setTypeface(null, Typeface.NORMAL) // Reset to normal text style
            }
            for (underline in underlines) {
                underline.visibility = View.GONE // Hide underline
            }
        }

        fun highlightTab(selectedTab: TextView, selectedUnderline: View) {
            selectedTab.setTextColor(ContextCompat.getColor(this, R.color.yellow)) // Set text color to yellow
            selectedTab.setTypeface(selectedTab.typeface, Typeface.BOLD) // Set text style to bold
            selectedUnderline.visibility = View.VISIBLE // Show underline
        }

        photosTab.setOnClickListener {
            tabnumber = 1
            resetTabs()
            highlightTab(photosTab, photosUnderline)
            performSearch(database, "photo", searchBar.text.toString().trim())
        }

        illustrationsTab.setOnClickListener {
            tabnumber = 2
            resetTabs()
            highlightTab(illustrationsTab, illustrationsUnderline)
            performSearch(database, "illustration", searchBar.text.toString().trim())
        }

        videosTab.setOnClickListener {
            tabnumber = 3
            resetTabs()
            highlightTab(videosTab, videosUnderline)
            // Add specific functionality for videos
        }

        gifsTab.setOnClickListener {
            tabnumber = 4
            resetTabs()
            highlightTab(gifsTab, gifsUnderline)
            performSearch(database, "gif", searchBar.text.toString().trim())
        }
    }

    private fun performSearch(database: Database, category: String, searchText: String) {
        val query: String
        val params: Array<String>

        if (searchText.isNotEmpty()) {
            query = "SELECT uploader, image FROM imaginary WHERE category = ? AND search LIKE ?"
            params = arrayOf(category, "%$searchText%")
        } else {
            query = "SELECT uploader, image FROM imaginary WHERE category = ?"
            params = arrayOf(category)
            headerSlogan.text = "Explore More Below"
        }

        val cursor = database.readableDatabase.rawQuery(query, params)
        val dataList = mutableListOf<DataModel>()

        if (cursor.moveToFirst()) {
            do {
                val uploader = cursor.getString(cursor.getColumnIndexOrThrow("uploader"))
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"))
                dataList.add(DataModel(uploader, image))
            } while (cursor.moveToNext())
        }
        cursor.close()

        val recyclerView = findViewById<RecyclerView>(R.id.frameContainer)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(this, dataList)
    }

    private fun addSampleGifRecords(database: Database) {
        val records = listOf(
            GifRecord(
                author = "Mary",
                search = "i love you",
                category = "gif",
                likes = 200,
                downloads = 120,
                comments = 15,
                gifData = getGifAsByteArray(R.drawable.gif1)
            ),
            GifRecord(
                author = "Rem",
                search = "cry",
                category = "gif",
                likes = 150,
                downloads = 80,
                comments = 5,
                gifData = getGifAsByteArray(R.drawable.gif2)
            ),
            GifRecord(
                author = "Remmer",
                search = "no",
                category = "gif",
                likes = 300,
                downloads = 200,
                comments = 25,
                gifData = getGifAsByteArray(R.drawable.gif3)
            )
        )

        for (record in records) {
            database.addGifRecord(
                record.author,
                record.search,
                record.category,
                record.likes,
                record.downloads,
                record.comments,
                record.gifData
            )
        }
    }

    private fun getGifAsByteArray(resourceId: Int): ByteArray {
        val inputStream = resources.openRawResource(resourceId)
        return inputStream.readBytes()
    }

    data class GifRecord(
        val author: String,
        val search: String,
        val category: String,
        val likes: Int,
        val downloads: Int,
        val comments: Int,
        val gifData: ByteArray
    )
}
