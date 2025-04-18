package com.example.numbuddy.utility

data class User(
    var username: String,
    var password: String,
    var email: String,
    var stageLevel: Int = 5
)

object UserManager {
    private val users = mutableListOf<User>()

    fun addUser(username: String, password: String, email: String): Int {
        if (users.any { it.username == username }) {
            return 0
        }
        if (users.any { it.email == email }) {
            return -1
        }
        users.add(User(username, password, email))
        return 1
    }

    fun loginUser(username: String, password: String): User? {
        return users.find { it.username == username && it.password == password }
    }

    fun updateUser(currentUser: User, username: String, email: String, password: String) {
        currentUser.username = username
        currentUser.email = email
        currentUser.password = password
    }

    // New method to increment the stage level of a user
    fun incrementUserStageLevel(username: String): Boolean {
        val user = users.find { it.username == username }
        return if (user != null) {
            user.stageLevel++
            true
        } else {
            false
        }
    }
}
