package com.example.project

data class Attendance(
    val id: Int,
    val user_id: Int,
    val course_id: Int,
    val date: String,
    val status: String
)