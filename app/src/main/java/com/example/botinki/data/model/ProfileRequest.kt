package com.example.botinki.data.model

data class ProfileRequest(
    val user_id: String,
    val photo: String,
    val firstname: String,
    val lastname: String,
    val address: String,
    val phone: String
)
