package com.example.businessapp.network

import com.example.businessapp.model.ForgetPasswordResponse
import com.example.businessapp.model.ForgotPasswordRequest
import com.example.businessapp.model.LoginRequest
import com.example.businessapp.model.LoginResponse
import com.example.businessapp.model.ResetPasswordRequest
import com.example.businessapp.model.VerifyOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("user/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    @POST("user/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ForgetPasswordResponse>
    @POST("user/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ForgetPasswordResponse>
    @POST("user/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<ForgetPasswordResponse>
}