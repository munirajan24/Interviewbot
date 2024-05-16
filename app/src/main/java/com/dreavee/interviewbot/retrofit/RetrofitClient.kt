package com.dreavee.interviewbot.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://large-text-to-speech.p.rapidapi.com"

    // Create a custom OkHttpClient without caching
    private val httpClient = OkHttpClient.Builder()
        .cache(null) // Disable caching
        .build()

    val apiService: com.dreavee.interviewbot.retrofit.ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(com.dreavee.interviewbot.retrofit.RetrofitClient.BASE_URL)
            .client(com.dreavee.interviewbot.retrofit.RetrofitClient.httpClient) // Use the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(com.dreavee.interviewbot.retrofit.ApiService::class.java)
    }
}