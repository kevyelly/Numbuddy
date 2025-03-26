package com.example.numbuddy

import com.example.numbuddy.utility.SessionManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        val user = SessionManager.loggedInUser
        val profileName = findViewById<TextView>(R.id.profilename)
        val profileEmail =  findViewById<TextView>(R.id.emailadd)
        profileName.text = user!!.username
        profileEmail.text = user.email

    }
}