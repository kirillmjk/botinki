package com.example.botinki.data.model

data class ChangePasswordRequest(
    val email: String,
    val newPassword: String
)
