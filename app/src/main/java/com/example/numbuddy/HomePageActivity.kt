package com.example.numbuddy

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

import com.example.numbuddy.R

class HomePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val user = SessionManager.loggedInUser


        val btnProfile = findViewById<ImageView>(R.id.profilebtn)
        val btnSettings = findViewById<ImageView>(R.id.settingsbtn)
        val usernamePlacer = findViewById<TextView>(R.id.usernameplacer)
        val greetinguser = findViewById<TextView>(R.id.msg1)

        usernamePlacer.text = user!!.username
        val greeting = "Good Day " + user.username + "!"
        greetinguser.text = greeting

        btnProfile.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        btnSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

}

