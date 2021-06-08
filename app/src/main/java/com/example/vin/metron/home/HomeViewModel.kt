package com.example.vin.metron.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vin.metron.data.remote.network.ApiConfig
import com.example.vin.metron.data.remote.network.ApiService
import com.example.vin.metron.data.remote.network.ResponseIsFake
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel() {
    private val apiService: ApiService = ApiConfig.provideApiService()

    fun checkIsFakeFromURI(image: MultipartBody.Part,context: Context): LiveData<ResponseIsFake> {
        val result: MutableLiveData<ResponseIsFake> = MutableLiveData<ResponseIsFake>()
        val client = apiService.checkIsFakeFromURI(image)
        client.enqueue(object : Callback<ResponseIsFake> {
            override fun onResponse(
                call: retrofit2.Call<ResponseIsFake>,
                response: Response<ResponseIsFake>
            ) {
                Log.d("hv_model",response.body().toString())
                result.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<ResponseIsFake>, t: Throwable) {
                Toast.makeText(context,"Error Api",Toast.LENGTH_SHORT).show()
            }
        })
        return result
    }
}