package com.example.numbuddy.utility

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson // Import Gson
import com.google.gson.reflect.TypeToken // Import TypeToken


data class User(
    var username: String,
    var password: String,
    var email: String

)

object UserManager {

    private const val PREFS_NAME = "UserAccountPrefs"
    private const val KEY_USER_LIST = "user_list_json"
    private val gson = Gson()
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    private fun loadUserList(context: Context): MutableList<User> {
        val prefs = getPreferences(context)
        val json = prefs.getString(KEY_USER_LIST, null)
        return if (json != null) {
            try {

                val type = object : TypeToken<MutableList<User>>() {}.type
                gson.fromJson(json, type) ?: mutableListOf()
            } catch (e: Exception) {
                Log.e("UserManager", "Error parsing user list JSON", e)
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
    }

    private fun saveUserList(context: Context, users: List<User>) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        try {
            val json = gson.toJson(users)
            editor.putString(KEY_USER_LIST, json)
            editor.apply() // Save changes
        } catch (e: Exception) {
            Log.e("UserManager", "Error saving user list JSON", e)
        }
    }

    fun addUser(context: Context, username: String, password: String, email: String): Int {
        val users = loadUserList(context)

        if (users.any { it.username.equals(username, ignoreCase = true) }) {
            return 0
        }
        if (users.any { it.email.equals(email, ignoreCase = true) }) {
            return -1
        }

        users.add(User(username, password, email))
        saveUserList(context, users)
        Log.d("UserManager", "User added: $username")
        return 1
    }

    fun loginUser(context: Context, username: String, password: String): User? {
        val users = loadUserList(context)
        val foundUser = users.find { it.username.equals(username, ignoreCase = true) && it.password == password }
        if (foundUser != null) {
            Log.d("UserManager", "User logged in: $username")
        } else {
            Log.d("UserManager", "Login failed for: $username")
        }
        return foundUser
    }


    fun updateUser(context: Context, currentUsername: String, newUsername: String, newEmail: String, newPassword: String): Boolean {
        val users = loadUserList(context)
        val userToUpdate = users.find { it.username.equals(currentUsername, ignoreCase = true) }

        if (userToUpdate == null) {
            Log.w("UserManager", "User not found for update: $currentUsername")
            return false
        }


        if (users.any { it.username.equals(newUsername, ignoreCase = true) && it != userToUpdate }) {
            Log.w("UserManager", "Update failed: New username '$newUsername' already taken.")
            return false
        }
        if (users.any { it.email.equals(newEmail, ignoreCase = true) && it != userToUpdate }) {
            Log.w("UserManager", "Update failed: New email '$newEmail' already taken.")
            return false
        }

        userToUpdate.username = newUsername
        userToUpdate.email = newEmail
        userToUpdate.password = newPassword

        saveUserList(context, users)
        Log.d("UserManager", "User updated. Old: $currentUsername, New: $newUsername")
        return true
    }

}