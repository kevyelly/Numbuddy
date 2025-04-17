package com.example.numbuddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.numbuddy.utility.UserManager


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
        val confirmPassword = findViewById<EditText>(R.id.confirmpassword)

        signinredirect.setOnClickListener{
            finish()
        }
        signUpButton.setOnClickListener {
            if(userNameField.text.isNullOrEmpty() || passwordField.text.isNullOrEmpty() || emailField.text.isNullOrEmpty() || confirmPassword.text.isNullOrEmpty()){
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailField.text.trim()).matches()){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val password = passwordField.text.toString()
            val hasNumber = password.contains(Regex("\\d"))
            val hasSpecial = password.contains(Regex("[!@#\$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>/?]"))

            if (!hasNumber || !hasSpecial) {
                Toast.makeText(this, "Password must contain at least one number and one special character", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(passwordField.text.toString().trim() != confirmPassword.text.toString().trim()){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val res = UserManager.addUser(userNameField.text.toString().trim(), passwordField.text.toString().trim(), emailField.text.toString().trim())
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
                    val username =  userNameField.text.toString().trim()
                    val password = passwordField.text.toString().trim();
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.putExtra("Username", username)
                    intent.putExtra("Password", password)
                    startActivity(intent)
                    finish()
                }
            }



        }


    }
}