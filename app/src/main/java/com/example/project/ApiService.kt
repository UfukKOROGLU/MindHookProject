package com.example.project

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/participation/{user_id}")
    fun getParticipationScore(@Path("user_id") userId: Int): Call<Participation>

    @POST("api/login")
    fun loginUser(@Body credentials: LoginRequest): Call<LoginResponse>

    @GET("/attendance/{user_id}")
    fun getAttendanceList(@Path("user_id") userId: Int): Call<List<Attendance>>

    @GET("/profile/{user_id}")
    fun getProfile(@Path("user_id") userId: Int): Call<UserProfile>

    @GET("/courses")
    fun getCourses(): Call<List<Course>>

}

