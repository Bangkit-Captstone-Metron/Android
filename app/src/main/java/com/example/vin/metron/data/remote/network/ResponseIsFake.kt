package com.example.vin.metron.data.remote.network

import com.google.gson.annotations.SerializedName

data class ResponseIsFake(
    @SerializedName("fake")
    var isFake: Boolean = false,

    @SerializedName("confidence")
    var confidence: Double = 100.00
)