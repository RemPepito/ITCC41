package com.example.itcc41

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val toggleTextView = findViewById<TextView>(R.id.toggleTextView)

        toggleTextView.setOnClickListener {
            // Start RegisterActivity when "Signup!" is clicked
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateCredentials(username, password)) {
                // Navigate to PageActivity after successful login
                val intent = Intent(this, PageActivity::class.java)
                startActivity(intent)
                finish() // Close the current activity to prevent going back to login
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateCredentials(username: String, password: String): Boolean {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userData = sharedPreferences.getString("user_data", null)

        if (userData != null) {
            val userArray = JSONArray(userData)

            // Iterate through the JSON array to find a matching username and password
            for (i in 0 until userArray.length()) {
                val userObject = userArray.getJSONObject(i)
                val storedUsername = userObject.getString("username")
                val storedPassword = userObject.getString("password")

                if (username == storedUsername && password == storedPassword) {
                    return true // Valid credentials
                }
            }
        }

        return false // Invalid credentials
    }
}
