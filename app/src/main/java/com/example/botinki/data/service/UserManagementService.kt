package com.example.botinki.data.service

import com.example.botinki.data.model.*
import retrofit2.Response
import retrofit2.http.*

const val API_KEY =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5kbmVua2VhemJvZ2ZsZHVqYndkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjkwODE3NjgsImV4cCI6MjA4NDY1Nzc2OH0.7Z6YoDkq3QeptjsdsQl6CNd-_hENPNW3d4U_1-FBymo"



interface UserManagementService {
    @Headers("apikey: $API_KEY", "Content-Type: " +
            "application/json")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>
}
