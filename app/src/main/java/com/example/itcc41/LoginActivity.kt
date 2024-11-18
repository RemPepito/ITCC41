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
import com.example.itcc41.R

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Handle window insets
        // Find views for main page
// Find views for main page
        val photosTab: TextView = findViewById(R.id.photosTab)
        val illustrationsTab: TextView = findViewById(R.id.illustrationsTab)
        val videosTab: TextView = findViewById(R.id.videosTab)
        val gifsTab: TextView = findViewById(R.id.gifsTab)


        val photosUnderline: View = findViewById(R.id.photosUnderline)
        val illustrationsUnderline: View = findViewById(R.id.illustrationsUnderline)
        val videosUnderline: View = findViewById(R.id.videosUnderline)
        val gifsUnderline: View = findViewById(R.id.gifsUnderline)
        val searchBar: EditText = findViewById(R.id.searchMainBar)
        val headerSlogan: TextView = findViewById(R.id.results_text)


        fun resetTabs() {
            val tabs = listOf(photosTab, illustrationsTab, videosTab, gifsTab)
            val underlines =
                listOf(photosUnderline, illustrationsUnderline, videosUnderline, gifsUnderline)
            for (tab in tabs) {
                tab.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                ) // Reset text color to black
                tab.setTypeface(null, Typeface.NORMAL) // Reset to normal text style
            }
            for (underline in underlines) {
                underline.visibility = View.GONE // Hide underline
            }
        }


        fun highlightTab(selectedTab: TextView, selectedUnderline: View) {
            selectedTab.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.yellow
                )
            ) // Set text color to yellow
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
        searchBar.setOnEditorActionListener { v, actionId, event ->
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

