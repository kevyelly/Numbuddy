package com.example.numbuddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button // Changed import assuming signUpButton is a Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.numbuddy.utility.UserManager
// --- ADD THIS IMPORT ---
import com.example.numbuddy.utility.UserProgressManager

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val signinredirect = findViewById<TextView>(R.id.redirectloginclickable)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val userNameField = findViewById<EditText>(R.id.usernamefield)
        val passwordField = findViewById<EditText>(R.id.passwordfield)
        val emailField = findViewById<EditText>(R.id.emailfield)
        val confirmPassword = findViewById<EditText>(R.id.confirmpassword)

        signinredirect.setOnClickListener{
            finish()
        }

        signUpButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

            val usernameInput = userNameField.text.toString().trim()
            val passwordInput = passwordField.text.toString().trim()
            val emailInput = emailField.text.toString().trim()
            val confirmPasswordInput = confirmPassword.text.toString().trim()

            if(usernameInput.isEmpty() || passwordInput.isEmpty() || emailInput.isEmpty() || confirmPasswordInput.isEmpty()){
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(passwordInput != confirmPasswordInput){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val hasNumber = passwordInput.contains(Regex("\\d"))
            val hasSpecial = passwordInput.contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`]"))
            if (!hasNumber || !hasSpecial) {
                Toast.makeText(this, "Password must contain at least one number and one special character", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (passwordInput.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val res = UserManager.addUser(this,usernameInput, passwordInput, emailInput)

            when (res) {
                0 -> {
                    Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
                }
                -1 -> {
                    Toast.makeText(this, "Email is already used", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()

                    UserProgressManager.saveUserStageLevel(this, usernameInput, 1)

                    val intent = Intent(this, SignInActivity::class.java)
                    intent.putExtra("Username", usernameInput)
                    intent.putExtra("Password", passwordInput)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                else -> {

                    Toast.makeText(this, "An unknown error occurred during sign up.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}