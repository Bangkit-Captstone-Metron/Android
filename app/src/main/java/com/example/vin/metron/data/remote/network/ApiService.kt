package com.example.vin.metron.data.remote.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("file_fake_checker")
    fun checkIsFakeFromURI(
        @Part image: MultipartBody.Part,
    ): Call<ResponseIsFake>

    @POST("fake_checker")
    fun checkIsFakeFromURL(
        @Body hashMap: HashMap<String, String>
    ): Call<ResponseIsFake>
}