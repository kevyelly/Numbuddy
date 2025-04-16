package com.example.numbuddy

import android.content.Intent
import com.example.numbuddy.utility.SessionManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.numbuddy.utility.LevelManager

class HomePageFragment : Fragment() {

    private lateinit var levelManager: LevelManager
    private lateinit var lvlButtons: List<Button>
    private lateinit var usernamePlacer: TextView
    private lateinit var greetinguser: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN

        levelManager = LevelManager(requireContext())

        usernamePlacer = view.findViewById(R.id.usernameplacer)
        greetinguser = view.findViewById(R.id.msg1)

        val lvl1button = view.findViewById<Button>(R.id.lvl1Button)
        val lvl2button = view.findViewById<Button>(R.id.lvl2Button)
        val lvl3button = view.findViewById<Button>(R.id.lvl3Button)
        val lvl4button = view.findViewById<Button>(R.id.lvl4Button)
        val lvl5button = view.findViewById<Button>(R.id.lvl5Button)
        val lvl6button = view.findViewById<Button>(R.id.lvl6Button)

        lvlButtons = listOf(lvl1button, lvl2button, lvl3button, lvl4button, lvl5button, lvl6button)

        lvl1button.setOnClickListener { launchQuizWithType(1) }
        lvl2button.setOnClickListener { launchQuizIfUnlocked(2) }
        lvl3button.setOnClickListener { launchQuizIfUnlocked(3) }
        lvl4button.setOnClickListener { launchQuizIfUnlocked(4) }
        lvl5button.setOnClickListener { launchQuizIfUnlocked(5) }
        lvl6button.setOnClickListener { launchQuizIfUnlocked(6) }
    }

    override fun onResume() {
        super.onResume()
        val user = SessionManager.loggedInUser
        if (user != null) {
            usernamePlacer.text = user.username
            greetinguser.text = "Good Day ${user.username}!"

            for ((index, button) in lvlButtons.withIndex()) {
                val level = index + 1
                when {
                    user.stageLevel > level -> {
                        button.text = "REPLAY"
                        button.isEnabled = true
                    }
                    user.stageLevel == level -> {
                        button.text = "START"
                        button.isEnabled = true
                    }
                    else -> {
                        button.text = "\uD83D\uDD12"
                        button.isEnabled = false
                    }
                }
            }
        }
    }

    private fun launchQuizWithType(type: Int) {
        val intent = Intent(requireContext(), QuizActivity::class.java)
        intent.putExtra("QUESTION_TYPE", type)
        startActivity(intent)
    }

    private fun launchQuizIfUnlocked(type: Int) {
        val user = SessionManager.loggedInUser
        if (user != null && user.stageLevel >= type) {
            launchQuizWithType(type)
        }
    }
}

