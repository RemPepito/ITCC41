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
    private lateinit var gifsTab: TextView

    private lateinit var photosUnderline: View
    private lateinit var illustrationsUnderline: View
    private lateinit var gifsUnderline: View
    private lateinit var searchBar: EditText
    private lateinit var headerSlogan: TextView

    private var tabnumber = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Database(this)

        // Initialize views
        photosTab = findViewById(R.id.photosTab)
        illustrationsTab = findViewById(R.id.illustrationsTab)
        gifsTab = findViewById(R.id.gifsTab)

        photosUnderline = findViewById(R.id.photosUnderline)
        illustrationsUnderline = findViewById(R.id.illustrationsUnderline)
        gifsUnderline = findViewById(R.id.gifsUnderline)
        searchBar = findViewById(R.id.searchMainBar)
        headerSlogan = findViewById(R.id.results_text)

        fun resetTabs() {
            val tabs = listOf(photosTab, illustrationsTab, gifsTab)
            val underlines = listOf(photosUnderline, illustrationsUnderline, gifsUnderline)
            for (tab in tabs) {
                tab.setTextColor(ContextCompat.getColor(this, R.color.black))
                tab.setTypeface(null, Typeface.NORMAL)
            }
            for (underline in underlines) {
                underline.visibility = View.GONE
            }
        }

        fun highlightTab(selectedTab: TextView, selectedUnderline: View) {
            selectedTab.setTextColor(ContextCompat.getColor(this, R.color.yellow))
            selectedTab.setTypeface(selectedTab.typeface, Typeface.BOLD)
            selectedUnderline.visibility = View.VISIBLE
        }

        fun performSearch(category: String, searchText: String) {
            val query: String
            val params: Array<String>

            if (searchText.isNotEmpty()) {
                query = "SELECT _id, uploader, image FROM imaginary WHERE category = ? AND search LIKE ?"
                params = arrayOf(category, "%$searchText%")
            } else {
                query = "SELECT _id, uploader, image FROM imaginary WHERE category = ?"
                params = arrayOf(category)
                headerSlogan.text = "Explore More Below"
            }

            val cursor = database.readableDatabase.rawQuery(query, params)
            val dataList = mutableListOf<DataModel>()

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("_id")) // Fetch ID
                    val uploader = cursor.getString(cursor.getColumnIndexOrThrow("uploader"))
                    val image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"))
                    dataList.add(DataModel(id, uploader, image, "", category, 0, 0, 0))
                } while (cursor.moveToNext())
            }
            cursor.close()

            // Assuming frameContainer is actually a RecyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.frameContainer)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = RecyclerViewAdapter(this, dataList, isGif = false)
        }

        fun performGifSearch(category: String, searchText: String) {
            val query: String
            val params: Array<String>

            if (searchText.isNotEmpty()) {
                query = "SELECT id, uploader, gif FROM gifs WHERE search LIKE ?"
                params = arrayOf("%$searchText%")
                headerSlogan.text = "Results for \"$searchText\""
            } else {
                query = "SELECT id, uploader, gif FROM gifs"
                params = arrayOf()
                headerSlogan.text = "Explore More Below"
            }

            val cursor = database.readableDatabase.rawQuery(query, params)
            val dataList = mutableListOf<DataModel>()

            if (cursor.moveToFirst()) {
                do {
                    // Fetch the 'id' correctly from the cursor
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val uploader = cursor.getString(cursor.getColumnIndexOrThrow("uploader"))
                    val gifData = cursor.getBlob(cursor.getColumnIndexOrThrow("gif"))

                    // Add the correct 'id' to the DataModel
                    dataList.add(DataModel(id, uploader, gifData, "", category, 0, 0, 0))
                } while (cursor.moveToNext())
            }
            cursor.close()

            // Set up RecyclerView for GIFs
            val recyclerView = findViewById<RecyclerView>(R.id.frameContainer)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = RecyclerViewAdapter(this, dataList, isGif = true)
        }

        resetTabs()
        highlightTab(photosTab, photosUnderline)
        performSearch("photo", searchBar.text.toString().lowercase().trim())
        photosTab.setOnClickListener {
            tabnumber = 1
            resetTabs()
            highlightTab(photosTab, photosUnderline)
            performSearch("photo", searchBar.text.toString().lowercase().trim())
        }

        illustrationsTab.setOnClickListener {
            tabnumber = 2
            resetTabs()
            highlightTab(illustrationsTab, illustrationsUnderline)
            performSearch("illustration", searchBar.text.toString().lowercase().trim())
        }

        gifsTab.setOnClickListener {
            tabnumber = 3
            resetTabs()
            highlightTab(gifsTab, gifsUnderline)
            performGifSearch("gif", searchBar.text.toString().lowercase().trim())
        }

        searchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            ) {
                val searchText = searchBar.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    headerSlogan.text = "Results for \"$searchText\""
                }
                when (tabnumber) {
                    1 -> performSearch("photo", searchText)
                    2 -> performSearch("illustration", searchText)
                    3 -> performGifSearch("gif", searchText)
                }
                true
            } else {
                false
            }
        }

    }
}
