package com.dreamwin.interviewbot.model

import com.google.gson.annotations.SerializedName

data class TtsRapidAPIResult (

    @SerializedName("id"     ) var id     : String? = null,
    @SerializedName("status" ) var status : String? = null,
    @SerializedName("eta"    ) var eta    : Int?    = null,
    @SerializedName("text"   ) var text   : String? = null

)