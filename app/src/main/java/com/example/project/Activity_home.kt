package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Activity_Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val buttonFaceCheckIn = findViewById<Button>(R.id.buttonFaceCheckIn)
        val btnAttendanceHistory = findViewById<Button>(R.id.btnAttendanceHistory)
        val btnEngagementScore = findViewById<Button>(R.id.btnEngagementScore)
        val btnAlertsReminders = findViewById<Button>(R.id.btnAlertsReminders)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        buttonFaceCheckIn.setOnClickListener {
            val intent = Intent(this, Activity_face_check_in::class.java)
            startActivity(intent)
        }

        btnAttendanceHistory.setOnClickListener {
            val intent = Intent(this, Activity_attendance_history::class.java)
            startActivity(intent)
        }

        btnEngagementScore.setOnClickListener {
            val intent = Intent(this, Activity_engagement_score::class.java)
            startActivity(intent)
        }

        btnAlertsReminders.setOnClickListener {
            val intent = Intent(this, Activity_alerts_reminders::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, Activity_profile::class.java)
            startActivity(intent)
        }


    }
}