package com.example.interviewbot.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://large-text-to-speech.p.rapidapi.com"

    // Create a custom OkHttpClient without caching
    private val httpClient = OkHttpClient.Builder()
        .cache(null) // Disable caching
        .build()

    val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // Use the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}