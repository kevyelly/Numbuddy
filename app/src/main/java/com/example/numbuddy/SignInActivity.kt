package com.example.numbuddy

import com.example.numbuddy.utility.SessionManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.numbuddy.utility.UserManager


class SignInActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)

        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val userNameField = findViewById<EditText>(R.id.usernamefield)
        val passwordField = findViewById<EditText>(R.id.passwordfield)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signUpRedirect = findViewById<TextView>(R.id.signupredirectclickable)
        if(intent != null){
            val username = intent.getStringExtra("Username") ?: ""
            val password = intent.getStringExtra("Password") ?: ""
            userNameField.setText(username)
            passwordField.setText(password)
        }

        signInButton.setOnClickListener{
            if(userNameField.text.isNullOrEmpty() || passwordField.text.isNullOrEmpty()){
                Toast.makeText(this, "Username or Password must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = UserManager.loginUser(this, userNameField.text.toString(), passwordField.text.toString()) // Correct
            if (user != null) {
                SessionManager.login(user)
                Toast.makeText(this, "Successful Login", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed Login: Invalid Credentials", Toast.LENGTH_LONG).show()
            }


        }
        signUpRedirect.setOnClickListener{
            userNameField.text.clear()
            passwordField.text.clear()
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}