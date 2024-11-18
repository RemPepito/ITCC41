package com.example.itcc41

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
        val registeredUsername = sharedPreferences.getString("username", null)
        val registeredPassword = sharedPreferences.getString("password", null)

        // Check if the entered username and password match the saved credentials
        return username == registeredUsername && password == registeredPassword
    }
}
