package com.example.businessapp.model

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String,
    val device_token: String,
    val type: String,
    val social_id: String
)