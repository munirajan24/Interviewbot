package com.dreavee.interviewbot.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreavee.interviewbot.retrofit.RetrofitClient.apiService
import com.dreavee.interviewbot.model.TtsRapidAPIResult
import com.dreavee.interviewbot.model.TtsWithIdApiResult
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class MyViewModel : ViewModel() {


    private val ttsRapidAPIResultMutableLiveData = MutableLiveData<com.dreavee.interviewbot.model.TtsRapidAPIResult>()
    val ttsRapidAPIResultLiveData: LiveData<com.dreavee.interviewbot.model.TtsRapidAPIResult> get() = ttsRapidAPIResultMutableLiveData

    private val ttsWithIDMutableLiveData = MutableLiveData<com.dreavee.interviewbot.model.TtsWithIdApiResult>()
    val ttsWithIDResultLiveData: LiveData<com.dreavee.interviewbot.model.TtsWithIdApiResult> get() = ttsWithIDMutableLiveData

    fun postDataToApi(token: String, XRapidApiHost: String, requestBody: RequestBody) {
        viewModelScope.launch {
            try {
//                val apiResponse = apiService.postData("application/json",token,XRapidApiHost, requestBody)
                val apiResponse = apiService.postData(requestBody)
                if (apiResponse.isSuccessful) {
                    val responseBody = apiResponse.body()
                    ttsRapidAPIResultMutableLiveData.value = responseBody
                } else {
                    Log.d("apiService", "postDataToApi: error")
                    // Handle API error
                }
            } catch (e: Exception) {
                Log.d("apiService", "postDataToApi Exception: ${e.message}")
                // Handle network or other errors
            }
        }
    }

    fun getTtsWithId(map: HashMap<String, String>) {
        viewModelScope.launch {
            try {
                val apiResponse = apiService.getTtsWithId(map)
                if (apiResponse.isSuccessful) {
                    val responseBody = apiResponse.body()
                    ttsWithIDMutableLiveData.value = responseBody
                } else {
                    Log.d("apiService", "postDataToApi: error")
                    // Handle API error
                }
            } catch (e: Exception) {
                Log.d("apiService", "postDataToApi Exception: ${e.message}")
                // Handle network or other errors
            }
        }
    }
}
