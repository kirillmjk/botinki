package com.example.botinki.data.service

import com.example.botinki.data.model.*
import retrofit2.Response
import retrofit2.http.*

const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9na3NjaHpyenpkZWd4b2FyYXZkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI0ODY0ODksImV4cCI6MjA4ODA2MjQ4OX0.ppnJGU2gKAtAwXXdPp3nwrjMLE0_HUppCYzWyJii9yo"

data class ProfileDto(
    val id: String?,
    val user_id: String?,
    val photo: String?,
    val firstname: String?,
    val lastname: String?,
    val address: String?,
    val phone: String?,
    val created_at: String?
)
data class FavouriteDto(
    val id: String?,
    val product_id: String?,
    val user_id: String?
)

data class ProductDto(
    val id: String,
    val title: String,
    val category_id: String?,
    val cost: Double,
    val description: String,
    val is_best_seller: Boolean?
)

interface UserManagementService {
    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/verify")
    suspend fun verifyOTP(@Body verifyOtpRequest: VerifyOtpRequest): Response<Any>
    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/recover")
    suspend fun recoverPassword(@Body body: Map<String, String>): Response<Any>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("change-password")
    suspend fun changePassword(@Body body: ChangePasswordRequest): Response<Any>

    @Headers("apikey: $API_KEY", "Content-Type: application/json", "Authorization: Bearer $API_KEY")
    @POST("rest/v1/favourite")
    suspend fun addToFavourite(@Body favouriteRequest: FavouriteRequest): Response<Any>

    @Headers("apikey: $API_KEY")
    @GET("rest/v1/profiles")
    suspend fun getProfile(
        @Header("Authorization") authHeader: String,
        @Query("user_id") userIdFilter: String, // "eq.<uuid>"
        @Query("select") select: String = "*"
    ): Response<List<ProfileDto>>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @PATCH("rest/v1/profiles")
    suspend fun updateProfile(
        @Header("Authorization") authHeader: String,
        @Query("user_id") userIdFilter: String,
        @Body body: Map<String, Any>
    ): Response<Unit>

    @Headers("apikey: $API_KEY")
    @GET("rest/v1/products")
    suspend fun getProducts(
        @Header("Authorization") authHeader: String,
        @Query("select") select: String = "*"
    ): List<com.example.botinki.data.service.ProductDto>

    @Headers("apikey: $API_KEY")
    @GET("rest/v1/favourite")
    suspend fun getFavourites(
        @Header("Authorization") authHeader: String,
        @Query("user_id") userIdFilter: String, // "eq.<uuid>"
        @Query("select") select: String = "id,product_id,user_id"
    ): List<com.example.botinki.data.service.FavouriteDto>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @PATCH("rest/v1/profiles")
    suspend fun editProfile(
        @Header("Authorization") authHeader: String,
        @Query("user_id") userIdFilter: String,  // Добавили фильтр
        @Body body: ProfileRequest
    ): Response<Unit>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("rest/v1/profiles")
    suspend fun addProfile(
        @Header("Authorization") authHeader: String,
        @Body body: ProfileRequest
    ): Response<Unit>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("rest/v1/favourite")
    suspend fun addFavourite(
        @Header("Authorization") authHeader: String,
        @Body body: FavouriteRequest
    ): Response<Unit>

    @Headers("apikey: $API_KEY")
    @DELETE("rest/v1/favourite")
    suspend fun deleteFavourite(
        @Header("Authorization") authHeader: String,
        @Query("user_id") userIdFilter: String, // "eq.<uuid>"
        @Query("product_id") productIdFilter: String // "eq.<uuid>"
    ): Response<Unit>
}