package com.example.project

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_engagement_score : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_engagement_score)

        val progressBar = findViewById<ProgressBar>(R.id.progressBarScore)
        val textValue = findViewById<TextView>(R.id.textViewScore)
        val textMessage = findViewById<TextView>(R.id.textViewMessage)

        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            ApiClient.apiService.getParticipationScore(userId).enqueue(object : Callback<Participation> {
                override fun onResponse(call: Call<Participation>, response: Response<Participation>) {
                    if (response.isSuccessful && response.body() != null) {
                        val scoreRaw = response.body()?.score?.toString()
                        val score = scoreRaw?.toDoubleOrNull()?.toInt() ?: 0

                        progressBar.progress = score
                        textValue.text = "$score%"

                        if (score < 50) {
                            textMessage.text = "Your engagement score is low! 😟"
                            textMessage.setTextColor(Color.RED)
                        } else {
                            textMessage.text = "Great participation! Keep it up! 🎉"
                            textMessage.setTextColor(Color.BLACK)
                        }
                    } else {
                        Toast.makeText(this@Activity_engagement_score, "No score found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Participation>, t: Throwable) {
                    Log.e("EngagementScore", "API ERROR: ${t.message}")
                }
            })
        } else {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show()
        }
    }
}