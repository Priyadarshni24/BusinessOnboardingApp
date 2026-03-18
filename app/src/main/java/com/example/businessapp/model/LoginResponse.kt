package com.example.businessapp.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?
)