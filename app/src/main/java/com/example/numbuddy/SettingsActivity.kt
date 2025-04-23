
package com.example.numbuddy

import com.example.numbuddy.utility.SessionManager
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns // Import for email validation
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.numbuddy.utility.UserManager
// --- Import UserProgressManager ---
import com.example.numbuddy.utility.UserProgressManager

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
        val bkbutton = findViewById<ImageView>(R.id.bckbutton)
        bkbutton.setOnClickListener{

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

        if (currentUser == null) {
            Toast.makeText(this, "Error: Not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        editUsername.setText(currentUser.username)
        editEmail.setText(currentUser.email)
        editPassword.setText(currentUser.password)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()

        saveButton.setOnClickListener {
            val oldUsername = currentUser.username
            val newUsername = editUsername.text.toString().trim()
            val newEmail = editEmail.text.toString().trim()
            val newPassword = editPassword.text.toString().trim()

            if (newUsername.isBlank() || newEmail.isBlank() || newPassword.isBlank()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usernameChanged = oldUsername != newUsername
            var migrationAttempted = false
            var migrationNeeded = false

            if (usernameChanged) {
                migrationNeeded = true

                val tempUserCheck = UserManager.loginUser(this@SettingsActivity, newUsername, "any_password") // Correct - Added Context // Check if username exists
                if (tempUserCheck != null && tempUserCheck != currentUser) {
                    Toast.makeText(this, "Username '$newUsername' is already taken.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                try {
                    UserProgressManager.migrateUserProgress(this@SettingsActivity, oldUsername, newUsername)
                    migrationAttempted = true
                    android.util.Log.i("SettingsActivity", "Progress migration successful for $newUsername")
                } catch (e: Exception) {
                    migrationAttempted = false
                    android.util.Log.e("SettingsActivity", "Error migrating user progress", e)
                    Toast.makeText(this@SettingsActivity, "Error updating progress for new username.", Toast.LENGTH_LONG).show()

                }
            }

            val updateSuccess = UserManager.updateUser(this@SettingsActivity, oldUsername, newUsername, newEmail, newPassword) // Correct

            if (updateSuccess) {
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {

                Toast.makeText(this, "Update failed. Username or Email might already be in use.", Toast.LENGTH_LONG).show()
            }

        }

        dialog.show()
    }
}