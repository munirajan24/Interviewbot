package com.dreavee.interviewbot.model

import com.google.gson.annotations.SerializedName

data class TtsWithIdApiResult (

    @SerializedName("id"       ) var id      : String? = null,
    @SerializedName("status"   ) var status  : String? = null,
    @SerializedName("url"      ) var url     : String? = null,
    @SerializedName("job_time" ) var jobTime : Int?    = null

)