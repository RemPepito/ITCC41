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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            resetTabs()
            highlightTab(photosTab, photosUnderline)
        }

        illustrationsTab.setOnClickListener {
            resetTabs()
            highlightTab(illustrationsTab, illustrationsUnderline)
        }

        videosTab.setOnClickListener {
            resetTabs()
            highlightTab(videosTab, videosUnderline)
        }

        gifsTab.setOnClickListener {
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
                true // Indicates the event was handled
            } else {
                false
            }
        }
    }
}
