package com.example.project

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewEngagement = findViewById<TextView>(R.id.textViewEngagement)

        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            ApiClient.apiService.getProfile(userId).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    val userProfile = response.body()
                    userProfile?.let {
                        textViewName.text = "Name: ${it.username}"
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                }
            })

            ApiClient.apiService.getParticipationScore(userId).enqueue(object : Callback<Participation> {
                override fun onResponse(call: Call<Participation>, response: Response<Participation>) {
                    val participation = response.body()
                    participation?.let {
                        textViewEngagement.text = "Engagement Score: ${it.score}"
                    }
                }

                override fun onFailure(call: Call<Participation>, t: Throwable) {
                }
            })
        } else {
            Toast.makeText(this, "User ID not found.", Toast.LENGTH_SHORT).show()
        }
    }
}