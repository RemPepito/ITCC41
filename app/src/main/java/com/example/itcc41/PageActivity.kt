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

        fun performSearch(category: String, searchText: String) {
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

            // Assuming frameContainer is actually a RecyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.frameContainer)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = RecyclerViewAdapter(this, dataList)
        }

        photosTab.setOnClickListener {
            tabnumber = 1
            resetTabs()
            highlightTab(photosTab, photosUnderline)
            performSearch("photo", searchBar.text.toString().trim())
        }

        illustrationsTab.setOnClickListener {
            tabnumber = 2
            resetTabs()
            highlightTab(illustrationsTab, illustrationsUnderline)
            performSearch("illustration", searchBar.text.toString().trim())
        }


        gifsTab.setOnClickListener {
            tabnumber = 3
            resetTabs()
            highlightTab(gifsTab, gifsUnderline)
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
                }
                true // Indicates the event was handled
            } else {
                false
            }
        }
    }
}
