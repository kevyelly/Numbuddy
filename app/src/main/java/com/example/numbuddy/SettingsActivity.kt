package com.example.numbuddy

import com.example.numbuddy.utility.SessionManager
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.numbuddy.utility.UserManager

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val btnSignOut = findViewById<LinearLayout>(R.id.signoutbutton)
        btnSignOut.setOnClickListener{
            val intent = Intent(this, SignOutActivity::class.java)
            startActivity(intent)
        }
        val btnDev = findViewById<LinearLayout>(R.id.devsbutton)
        btnDev.setOnClickListener{
            val intent = Intent(this, DevActivity::class.java)
            startActivity(intent)
        }
        val bkbutton =  findViewById<ImageView>(R.id.bckbutton)
        bkbutton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val editProfile = findViewById<LinearLayout>(R.id.editprofilebutton)
        editProfile.setOnClickListener{
            showEditProfileDialog()
        }

    }
    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.edit_profile, null)
        val editUsername = dialogView.findViewById<EditText>(R.id.editUsername)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val editPassword = dialogView.findViewById<EditText>(R.id.editPassword)
        val saveButton = dialogView.findViewById<Button>(R.id.saveProfileButton)

        val currentUser = SessionManager.loggedInUser
        editUsername.setText(currentUser?.username)
        editEmail.setText(currentUser?.email)
        editPassword.setText(currentUser?.password)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Edit Profile")
            .setNegativeButton("Cancel", null)
            .create()
        saveButton.setOnClickListener {
            val newUsername = editUsername.text.toString()
            val newEmail = editEmail.text.toString()
            val newPassword = editPassword.text.toString()

            if (newUsername.isBlank() || newEmail.isBlank() || newPassword.isBlank()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            UserManager.updateUser(currentUser!!,newUsername, newEmail, newPassword)
            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
