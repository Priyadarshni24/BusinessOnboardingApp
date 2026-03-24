package com.example.businessapp.network

import com.example.businessapp.model.ForgetPasswordResponse
import com.example.businessapp.model.ForgotPasswordRequest
import com.example.businessapp.model.LoginRequest
import com.example.businessapp.model.LoginResponse
import com.example.businessapp.model.ResetPasswordRequest
import com.example.businessapp.model.VerifyOtpRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
    @Multipart
    @POST("user/register")
    suspend fun register(

        @Part("full_name") fullName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("role") role: RequestBody,

        @Part("business_name") businessName: RequestBody,
        @Part("informal_name") informalName: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("zip_code") zipCode: RequestBody,

        @Part registration_proof: MultipartBody.Part,

        @Part("business_hours") businessHours: RequestBody,

        @Part("device_token") deviceToken: RequestBody,
        @Part("type") type: RequestBody,
        @Part("social_id") socialId: RequestBody

    ): Response<ForgetPasswordResponse>

}