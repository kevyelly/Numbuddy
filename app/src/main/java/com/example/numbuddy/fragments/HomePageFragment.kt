package com.example.numbuddy.fragments

// --- Add necessary imports ---
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.numbuddy.QuizActivity
import com.example.numbuddy.R
import com.example.numbuddy.utility.SessionManager
import com.example.numbuddy.utility.UserProgressManager

class HomePageFragment : Fragment() {

    private lateinit var lvlButtons: List<Button>
    private lateinit var usernamePlacer: TextView
    private lateinit var greetinguser: TextView
    private lateinit var starctr: TextView

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

        usernamePlacer = view.findViewById(R.id.usernameplacer)
        greetinguser = view.findViewById(R.id.msg1)
        starctr = view.findViewById<TextView>(R.id.msg3)
        starctr.text = buildString {
            append("You have earned ")
            append(
                SessionManager.loggedInUser?.let {
                    UserProgressManager.getUserStars(
                        requireContext(),
                        it.username
                    )
                }
            )
            append(" \uD83C\uDF1F so far!")
        }

        val buttonIds = listOf(
            R.id.lvl1Button, R.id.lvl2Button, R.id.lvl3Button,
            R.id.lvl4Button, R.id.lvl5Button, R.id.lvl6Button
        )
        lvlButtons = buttonIds.mapNotNull { view.findViewById<Button?>(it) }
        lvlButtons.forEachIndexed { index, button ->
            val levelType = index + 1
            button.setOnClickListener {
                handleLevelButtonClick(levelType)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val user = SessionManager.loggedInUser
        if (user != null && isAdded && context != null) {
            usernamePlacer.text = user.username
            greetinguser.text = "Good Day, ${user.username}!"
            val currentUserStageLevel = UserProgressManager.getUserStageLevel(requireContext(), user.username)
            for ((index, button) in lvlButtons.withIndex()) {
                val levelForButton = index + 1
                when {
                    currentUserStageLevel > levelForButton -> {
                        button.text = "LESSON"
                        button.isEnabled = true
                    }
                    currentUserStageLevel == levelForButton -> {
                        button.text = "LESSON"
                        button.isEnabled = true
                    }
                    else -> {
                        button.text = "\uD83D\uDD12"
                        button.isEnabled = false
                    }
                }
            }
        } else if (user == null && isAdded) {
            Log.w("HomePageFragment", "User is null in onResume, cannot update UI.")
        }
    }

    private fun handleLevelButtonClick(levelType: Int) {
        val user = SessionManager.loggedInUser
        if (user != null && isAdded && context != null) {
            val currentUserStageLevel = UserProgressManager.getUserStageLevel(requireContext(), user.username)
            if (currentUserStageLevel >= levelType) {
                showLessonInfoDialog(levelType)
            } else {
                Toast.makeText(requireContext(), "Level locked!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.w("HomePageFragment", "Cannot handle level button click: User=$user, isAdded=$isAdded, context=$context")
            Toast.makeText(requireContext(), "Please log in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLessonInfoDialog(levelType: Int) {
        val ctx = context ?: return
        if (!isAdded || ctx.resources == null) return

        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)


        val dialogView = LayoutInflater.from(ctx).inflate(R.layout.lesson, null)
        dialog.setContentView(dialogView)


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val titleTextView = dialogView.findViewById<TextView>(R.id.lesson_title_textview)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.lesson_text_area)

        val youtubeImageView1 = dialogView.findViewById<ImageView>(R.id.lesson_youtube_image_1)
        val youtubeImageView2 = dialogView.findViewById<ImageView>(R.id.lesson_youtube_image_2)
        val youtubeCardView1 = dialogView.findViewById<View>(R.id.lesson_youtube_card_1)
        val youtubeCardView2 = dialogView.findViewById<View>(R.id.lesson_youtube_card_2)
        val startQuizButton = dialogView.findViewById<Button>(R.id.lesson_start_quiz_button)

        var lessonTitle = ""
        var lessonDescription = ""
        var youtubeUrl1 = ""
        var youtubeUrl2 = ""
        var thumbnailResId1 = R.drawable.settingimg
        var thumbnailResId2 = R.drawable.settingimg

        try {
            when (levelType) {
                1 -> {
                    lessonTitle = getString(R.string.level_1_title)
                    lessonDescription = getString(R.string.level_1_desc)
                    youtubeUrl1 = getString(R.string.level_1_url_1)
                    youtubeUrl2 = getString(R.string.level_1_url_2)
                    thumbnailResId1 = R.drawable.one1
                    thumbnailResId2 = R.drawable.one2
                }
                2 -> {
                    lessonTitle = getString(R.string.level_2_title)
                    lessonDescription = getString(R.string.level_2_desc)
                    youtubeUrl1 = getString(R.string.level_2_url_1)
                    youtubeUrl2 = getString(R.string.level_2_url_2)
                    thumbnailResId1 = R.drawable.two1
                    thumbnailResId2 = R.drawable.two2
                }
                3 -> {
                    lessonTitle = getString(R.string.level_3_title)
                    lessonDescription = getString(R.string.level_3_desc)
                    youtubeUrl1 = getString(R.string.level_3_url_1)
                    youtubeUrl2 = getString(R.string.level_3_url_2)
                    thumbnailResId1 = R.drawable.three1
                    thumbnailResId2 = R.drawable.three2
                }
                4 -> {
                    lessonTitle = getString(R.string.level_4_title)
                    lessonDescription = getString(R.string.level_4_desc)
                    youtubeUrl1 = getString(R.string.level_4_url_1)
                    youtubeUrl2 = getString(R.string.level_4_url_2)
                    thumbnailResId1 = R.drawable.four1
                    thumbnailResId2 = R.drawable.four2
                }
                5 -> {
                    lessonTitle = getString(R.string.level_5_title)
                    lessonDescription = getString(R.string.level_5_desc)
                    youtubeUrl1 = getString(R.string.level_5_url_1)
                    youtubeUrl2 = getString(R.string.level_5_url_2)
                    thumbnailResId1 = R.drawable.five1
                    thumbnailResId2 = R.drawable.five2
                }
                6 -> {
                    lessonTitle = getString(R.string.level_6_title)
                    lessonDescription = getString(R.string.level_6_desc)
                    youtubeUrl1 = getString(R.string.level_6_url_1)
                    youtubeUrl2 = getString(R.string.level_6_url_2)
                    thumbnailResId1 = R.drawable.six1
                    thumbnailResId2 = R.drawable.six2
                }
                else -> {
                    lessonTitle = "Lesson $levelType"
                    lessonDescription = "Content unavailable."
                }
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("HomePageFragment", "String or Drawable resource not found for level $levelType", e)
            Toast.makeText(requireContext(), "Lesson content/images not found.", Toast.LENGTH_SHORT).show()
            lessonTitle = "Lesson $levelType"
            lessonDescription = "Content unavailable."
        }


        titleTextView.text = lessonTitle
        descriptionTextView.text = lessonDescription

        try {
            youtubeImageView1?.setImageResource(thumbnailResId1)
        } catch (e: Resources.NotFoundException) {
            Log.e("HomePageFragment", "Drawable not found for ID: $thumbnailResId1", e)
            youtubeImageView1?.setImageResource(R.drawable.settingimg)
        }
        try {
            youtubeImageView2?.setImageResource(thumbnailResId2)
        } catch (e: Resources.NotFoundException) {
            Log.e("HomePageFragment", "Drawable not found for ID: $thumbnailResId2", e)
            youtubeImageView2?.setImageResource(R.drawable.settingimg)
        }


        val finalUrl1 = youtubeUrl1
        youtubeCardView1?.setOnClickListener { openUrl(finalUrl1) }

        val finalUrl2 = youtubeUrl2
        youtubeCardView2?.setOnClickListener { openUrl(finalUrl2) }

        startQuizButton.setOnClickListener {
            dialog.dismiss()
            launchQuizWithType(levelType)
        }

        if (isAdded && activity != null && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
            dialog.show()

            try {
                val displayMetrics = resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels
                val dialogWidth = (screenWidth * 0.85).toInt()
                val dialogHeight = (screenHeight * 0.90).toInt()
                dialog.window?.setLayout(dialogWidth, dialogHeight)
            } catch (e: Exception) {
                Log.e("HomePageFragment", "Error setting dialog window size", e)
            }


        } else {
            Log.w("HomePageFragment", "Dialog not shown: Fragment not attached or Activity finishing.")
        }
    }


    private fun openUrl(url: String) {
        val ctx = context ?: return
        if (url.isBlank() || url.startsWith("PLACEHOLDER_URL")) {
            Toast.makeText(ctx, "Video link not set yet.", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(ctx, "Could not open link - No app found.", Toast.LENGTH_LONG).show()
            Log.e("HomePageFragment", "ActivityNotFoundException for URL: $url", e)
        } catch (e: Exception) {
            Toast.makeText(ctx, "Could not open link.", Toast.LENGTH_SHORT).show()
            Log.e("HomePageFragment", "Error opening URL: $url", e)
        }
    }

    private fun launchQuizWithType(type: Int) {
        val ctx = context ?: return
        if (!isAdded) return
        val intent = Intent(ctx, QuizActivity::class.java)
        intent.putExtra("QUESTION_TYPE", type)
        startActivity(intent)
    }

}