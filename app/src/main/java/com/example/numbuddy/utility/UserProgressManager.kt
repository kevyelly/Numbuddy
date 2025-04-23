package com.example.numbuddy.utility

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object UserProgressManager {

    private const val PREFS_NAME = "UserProgressPrefs"
    private const val KEY_STAGE_LEVEL_PREFIX = "stage_level_"
    private const val KEY_QUIZZES_COMPLETED_PREFIX = "quizzes_completed_"
    private const val KEY_STARS_PREFIX = "stars_"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserStageLevel(context: Context, username: String, level: Int) {
        if (username.isBlank()) return
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_STAGE_LEVEL_PREFIX + username, level)
        editor.apply()
    }

    fun getUserStageLevel(context: Context, username: String): Int {
        if (username.isBlank()) return 1
        return getPreferences(context).getInt(KEY_STAGE_LEVEL_PREFIX + username, 1)
    }

    fun saveQuizzesCompleted(context: Context, username: String, count: Int) {
        if (username.isBlank()) return
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_QUIZZES_COMPLETED_PREFIX + username, count)
        editor.apply()
    }

    fun getQuizzesCompleted(context: Context, username: String): Int {
        if (username.isBlank()) return 0
        return getPreferences(context).getInt(KEY_QUIZZES_COMPLETED_PREFIX + username, 0)
    }

    fun saveUserStars(context: Context, username: String, count: Int) {
        if (username.isBlank()) {
            Log.w("UserProgressManager", "Attempted to save stars with blank username.")
            return
        }
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_STARS_PREFIX + username, count)
        editor.apply()
    }

    fun getUserStars(context: Context, username: String): Int {
        if (username.isBlank()) {
            Log.w("UserProgressManager", "Attempted to get stars with blank username.")
            return 0
        }
        return getPreferences(context).getInt(KEY_STARS_PREFIX + username, 0)
    }

    fun clearUserProgress(context: Context, username: String) {
        if (username.isBlank()) return
        val editor = getPreferences(context).edit()
        editor.remove(KEY_STAGE_LEVEL_PREFIX + username)
        editor.remove(KEY_QUIZZES_COMPLETED_PREFIX + username)
        editor.remove(KEY_STARS_PREFIX + username)
        editor.apply()
        Log.i("UserProgressManager", "Cleared progress for '$username'")
    }

    fun clearAllProgress(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
        Log.i("UserProgressManager", "Cleared all progress data.")
    }
    fun migrateUserProgress(context: Context, oldUsername: String, newUsername: String) {
        val tag = "UserProgressManager"

        if (oldUsername.isBlank() || newUsername.isBlank()) {
            Log.w(tag, "Migration skipped: one or both usernames are blank.")
            return
        }
        if (oldUsername.equals(newUsername, ignoreCase = true)) {
            Log.w(tag, "Migration skipped: usernames are identical.")
            return
        }

        Log.i(tag, "Migrating progress from '$oldUsername' to '$newUsername'")

        val level = getUserStageLevel(context, oldUsername)
        val quizzesCompleted = getQuizzesCompleted(context, oldUsername)
        val stars = getUserStars(context, oldUsername)

        saveUserStageLevel(context, newUsername, level)
        saveQuizzesCompleted(context, newUsername, quizzesCompleted)
        saveUserStars(context, newUsername, stars)
        with(getPreferences(context).edit()) {
            remove("$KEY_STAGE_LEVEL_PREFIX$oldUsername")
            remove("$KEY_QUIZZES_COMPLETED_PREFIX$oldUsername")
            remove("$KEY_STARS_PREFIX$oldUsername")
            apply()
        }

        Log.i(tag, "Migration complete for '$newUsername'")
    }

}