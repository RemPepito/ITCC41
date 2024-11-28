package com.example.itcc41

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val toggleTextView = findViewById<TextView>(R.id.toggleTextView)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (name.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    saveUserData(name, password)
                    Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()

                    // Navigate back to MainActivity after successful registration
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Set OnClickListener for toggleTextView
        toggleTextView.setOnClickListener {
            // Navigate back to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveUserData(name: String, password: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Retrieve existing user data
        val userData = sharedPreferences.getString("user_data", null)
        val userArray = if (userData != null) {
            JSONArray(userData)
        } else {
            JSONArray()
        }

        // Check if the username already exists
        var userExists = false
        for (i in 0 until userArray.length()) {
            val userObject = userArray.getJSONObject(i)
            if (userObject.getString("username") == name) {
                userExists = true
                break
            }
        }

        if (userExists) {
            Toast.makeText(this, "Username already exists. Please choose a different one.", Toast.LENGTH_SHORT).show()
        } else {
            // Add new user to the array
            val newUser = JSONObject()
            newUser.put("username", name)
            newUser.put("password", password)
            userArray.put(newUser)

            // Save the updated array back to SharedPreferences
            editor.putString("user_data", userArray.toString())
            editor.apply()
        }
    }
}
