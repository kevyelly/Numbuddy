package com.example.numbuddy.utility

object SessionManager {
    var loggedInUser: User? = null

    fun login(user: User) {
        loggedInUser = user
    }

    fun logout() {
        loggedInUser = null
    }

    fun isLoggedIn(): Boolean {
        return loggedInUser != null
    }
}
