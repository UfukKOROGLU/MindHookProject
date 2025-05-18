package com.example.project

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_alerts_reminders : AppCompatActivity() {
    private lateinit var textViewAlert: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts_reminders)

        textViewAlert = findViewById(R.id.textViewAlert)

        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            ApiClient.apiService.getParticipationScore(userId).enqueue(object : Callback<Participation> {
                override fun onResponse(call: Call<Participation>, response: Response<Participation>) {
                    if (response.isSuccessful) {
                        val participation = response.body()
                        participation?.let {
                            if (it.score.toDouble() < 50.0) {
                                textViewAlert.visibility = View.VISIBLE
                                textViewAlert.text = "⚠️ Your engagement score is low! (Score: ${it.score}%)"
                            } else {
                                textViewAlert.visibility = View.GONE
                            }
                        }
                    } else {
                        Toast.makeText(this@Activity_alerts_reminders, "Failed to load engagement score.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Participation>, t: Throwable) {
                    Toast.makeText(this@Activity_alerts_reminders, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "User ID not found.", Toast.LENGTH_SHORT).show()
        }
    }
}