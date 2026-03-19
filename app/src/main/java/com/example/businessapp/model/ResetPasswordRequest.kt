package com.example.businessapp.model

data class ResetPasswordRequest(
    val token: String,
    val password: String,
    val cpassword: String
)