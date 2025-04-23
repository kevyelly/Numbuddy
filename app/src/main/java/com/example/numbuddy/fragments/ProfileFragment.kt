package com.example.numbuddy.fragments


import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.numbuddy.R
import com.example.numbuddy.utility.SessionManager
import com.example.numbuddy.utility.UserProgressManager
import kotlin.math.max



class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Set Fullscreen ---
        // Note: Accessing window directly might be better done in the Activity hosting the fragment
        // but this might work depending on the setup.
        try {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error setting fullscreen", e)
        }



        val profileName = view.findViewById<TextView>(R.id.profilename)
        val profileEmail = view.findViewById<TextView>(R.id.emailadd)


        val starsValueTextView = view.findViewById<TextView>(R.id.stars_value_text)
        val stagesValueTextView = view.findViewById<TextView>(R.id.stages_value_text)


        val user = SessionManager.loggedInUser


        if (user != null && context != null) {
            profileName?.text = user.username
            profileEmail?.text = user.email

            val username = user.username
            try {
                val stars = UserProgressManager.getUserStars(requireContext(), username)
                val stageLevel = UserProgressManager.getUserStageLevel(requireContext(), username)

                val stagesCompleted = max(0, stageLevel-1)

                starsValueTextView?.text = stars.toString()


                stagesValueTextView?.text = "$stagesCompleted completed"

            } catch (e: Resources.NotFoundException) {
                Log.e("ProfileFragment", "Resource not found (String/Drawable?)", e)
                Toast.makeText(requireContext(), "Error loading resources.", Toast.LENGTH_SHORT).show()

                starsValueTextView?.text = "0"
                stagesValueTextView?.text = "0 completed"
            }
            catch (e: Exception) {
                Log.e("ProfileFragment", "Error getting user progress", e)
                Toast.makeText(requireContext(), "Could not load progress", Toast.LENGTH_SHORT).show()

                starsValueTextView?.text = "0"
                stagesValueTextView?.text = "0 completed"
            }

        } else {

            Log.w("ProfileFragment", "User not logged in or context is null.")
            profileName?.text = "N/A"
            profileEmail?.text = "N/A"
            starsValueTextView?.text = "0"
            stagesValueTextView?.text = "0 completed"
        }
    }
}