package com.example.numbuddy.fragments

import com.example.numbuddy.utility.SessionManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.numbuddy.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = SessionManager.loggedInUser
        val profileName = view.findViewById<TextView>(R.id.profilename)
        val profileEmail = view.findViewById<TextView>(R.id.emailadd)
        profileName.text = user!!.username
        profileEmail.text = user.email


    }
}
