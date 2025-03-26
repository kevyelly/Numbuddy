package com.example.numbuddy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.numbuddy.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN


        val btnProfile = findViewById<ImageView>(R.id.profilebtn)
        val btnSettings = findViewById<ImageView>(R.id.settingsbtn)
        val btnHome = findViewById<ImageView>(R.id.homebtn)

        loadFragment(HomePageFragment())


        btnProfile.setOnClickListener{
            loadFragment(ProfileFragment())
        }
        btnSettings.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
        btnHome.setOnClickListener{
            loadFragment(HomePageFragment())
        }


    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
}

