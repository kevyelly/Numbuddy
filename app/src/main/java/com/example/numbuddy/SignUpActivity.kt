package com.example.numbuddy

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val signinredirect = findViewById<TextView>(R.id.redirectloginclickable)
        val signUpButton = findViewById<TextView>(R.id.signUpButton)
        val userNameField = findViewById<EditText>(R.id.usernamefield)
        val passwordField = findViewById<EditText>(R.id.passwordfield)
        val emailField = findViewById<EditText>(R.id.emailfield)

        signinredirect.setOnClickListener{
            finish()
        }
        signUpButton.setOnClickListener {
            if(userNameField.text.isNullOrEmpty() || passwordField.text.isNullOrEmpty() || emailField.text.isNullOrEmpty()){
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailField.text).matches()){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val res = UserManager.addUser(userNameField.text.toString(), passwordField.text.toString(), emailField.text.toString())
            when (res) {
                0 -> {
                    Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                -1 -> {
                    Toast.makeText(this, "Email is already used", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    Toast.makeText(this, "Successfully created account", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }
    }
}