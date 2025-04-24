# Numbuddy  BUDDY

## Overview

Numbuddy is a fun and engaging math learning application designed for kids, built for Android. It aims to teach basic math concepts through interactive quizzes and supplementary materials in a structured, level-based approach.

## Features

* **User Accounts:** Supports user sign-up and login to track individual progress. User credentials persist across app sessions using SharedPreferences.
* **Persistent Progress:** Saves user progress, including the highest level unlocked, quizzes completed, and stars earned, using SharedPreferences linked to the user account. Progress is maintained even if the username is changed.
* **Skill Levels:** Features multiple "Skill Steps" covering various math topics:
    * Level 1: Number Basics (Less/Greater Than)
    * Level 2: Intro to Addition
    * Level 3: Simple Subtraction
    * Level 4: Multiplication & Division
    * Level 5: Place Values
    * Level 6: Advanced Arithmetic (Large Numbers)
* **Interactive Quizzes:** Provides quizzes for each level with multiple-choice answers.
* **Animated Feedback:** Uses Lottie animations and text popups ("CORRECT!", "TRY AGAIN") for immediate feedback on answers. Feedback persists until tapped by the user or after a short delay.
* **Lesson Dialogs:** Presents a popup before each quiz with a kid-friendly description of the lesson topic and clickable thumbnails linking to supplementary YouTube videos.
* **Profile Screen:** Displays user information (username, email) and key progress statistics (Highest Level Passed, Stars Earned, Quizzes Completed).
* **Settings:** Allows users to edit their profile information and view developer details.

## Technology Stack

* **Language:** Kotlin
* **Platform:** Android (Target SDK 24 - Android 7.0 Nougat)
* **IDE:** Android Studio
* **Key Libraries:** AndroidX (AppCompat, Fragments, ConstraintLayout), Material Components (CardView, Button), Lottie (for animations), Gson (for SharedPreferences).

## Setup

1.  Clone the repository (if applicable).
2.  Open the project in Android Studio.
3.  Ensure you have an emulator or physical device running API 24 or higher.
4.  Build and run the application.

## Authors

* Karol Vincent Bebedor
* Julian Ramil Andales
