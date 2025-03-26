package com.example.numbuddy.utility

import android.content.Context
import android.content.SharedPreferences

class LevelManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("LevelProgress", Context.MODE_PRIVATE)

    fun setLevel1Completed(completed: Boolean) {
        prefs.edit().putBoolean("LEVEL_1_COMPLETED", completed).apply()
    }

    fun isLevel1Completed(): Boolean {
        return prefs.getBoolean("LEVEL_1_COMPLETED", false)
    }
}
