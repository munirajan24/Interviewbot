package com.dreamwin.interviewbot.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://large-text-to-speech.p.rapidapi.com"
    private const val GOOGLE_DRIVE_BASE_URL = "https://www.googleapis.com/"

    // Create a custom OkHttpClient without caching
    private val httpClient = OkHttpClient.Builder()
        .cache(null) // Disable caching
        .build()

    val apiService: com.dreamwin.interviewbot.retrofit.ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(com.dreamwin.interviewbot.retrofit.RetrofitClient.BASE_URL)
            .client(com.dreamwin.interviewbot.retrofit.RetrofitClient.httpClient) // Use the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(com.dreamwin.interviewbot.retrofit.ApiService::class.java)
    }

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(com.dreamwin.interviewbot.retrofit.RetrofitClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }
}