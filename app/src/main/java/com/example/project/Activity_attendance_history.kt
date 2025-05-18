package com.example.project

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_attendance_history : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_history)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerAttendance)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            ApiClient.apiService.getAttendanceList(userId).enqueue(object : Callback<List<Attendance>> {
                override fun onResponse(
                    call: Call<List<Attendance>>,
                    response: Response<List<Attendance>>
                ) {
                    if (response.isSuccessful) {
                        val attendanceList = response.body()
                        attendanceList?.forEach {
                            Log.d("Attendance", "${it.date} | ${it.course_id} | ${it.status}")
                        }

                        attendanceList?.let {
                            recyclerView.adapter = AttendanceAdapter(it)
                        }
                    } else {
                        Log.e("API", "No data: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Attendance>>, t: Throwable) {
                    Log.e("API", "BUG: ${t.message}")
                }
            })
        } else {
            Log.e("Attendance", "Unable to find an user ID!")
        }
    }
}