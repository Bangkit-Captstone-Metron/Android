package com.example.vin.metron.data.remote.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("fake_checker")
    fun checkIsFake(
        @Part("image") image: MultipartBody.Part
    ) : Call<ResponseIsFake>
}