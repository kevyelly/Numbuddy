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


        val user = SessionManager.loggedInUser
        val usernamePlacer = view.findViewById<TextView>(R.id.usernameplacer)
        val greetinguser = view.findViewById<TextView>(R.id.msg1)
        val lvl1button = view.findViewById<Button>(R.id.lvl1Button)

        usernamePlacer.text = user!!.username
        val greeting = "Good Day ${user.username}!"
        greetinguser.text = greeting
        lvl1button.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)
        }

    }
}
