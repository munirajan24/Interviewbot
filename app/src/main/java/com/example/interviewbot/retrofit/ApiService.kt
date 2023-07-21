package com.example.interviewbot.retrofit

import com.example.interviewbot.model.TtsRapidAPIResult
import com.example.interviewbot.model.TtsWithIdApiResult
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @Headers(
        "content-type: application/json",
        "X-RapidAPI-Key: 56f16c6520msh7fcb1e0ad4e4115p13ee2bjsnb90d1ba60fc9",
        "X-RapidAPI-Host: large-text-to-speech.p.rapidapi.com"
    )
    @POST("tts")
    suspend fun postData(
//        @Header("content-type") contentType: String,
//        @Header("X-RapidAPI-Key") token: String,
//        @Header("X-RapidAPI-Host") XRapidApiHost: String,
        @Body request: RequestBody
    ): Response<TtsRapidAPIResult>

    @Headers(
        "content-type: application/json",
        "X-RapidAPI-Key: 56f16c6520msh7fcb1e0ad4e4115p13ee2bjsnb90d1ba60fc9",
        "X-RapidAPI-Host: large-text-to-speech.p.rapidapi.com"
    )
    @GET("tts")
    suspend fun getTtsWithId(@QueryMap params: Map<String,String>): Response<TtsWithIdApiResult>
}