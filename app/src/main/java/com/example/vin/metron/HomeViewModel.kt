package com.example.vin.metron

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

    fun checkIsFakeFromURI(image: MultipartBody.Part): LiveData<ResponseIsFake> {
        val result: MutableLiveData<ResponseIsFake> = MutableLiveData<ResponseIsFake>()
        val client = apiService.checkIsFakeFromURI(image)
        client.enqueue(object : Callback<ResponseIsFake> {
            override fun onResponse(
                call: retrofit2.Call<ResponseIsFake>,
                response: Response<ResponseIsFake>
            ) {
                result.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<ResponseIsFake>, t: Throwable) {
                throw Error(t.message.toString())
            }
        })
        return result
    }

    /* Deprecated
    fun checkIsFakeFromURL(): LiveData<ResponseIsFake> {
        val result: MutableLiveData<ResponseIsFake> = MutableLiveData<ResponseIsFake>()
        val hashMap = HashMap<String, String>()
        hashMap.put(
            "image_url",
            "https://i.stack.imgur.com/MIe6s.png"
        )
        val client = apiService.checkIsFakeFromURL(hashMap)
        client.enqueue(object : Callback<ResponseIsFake> {
            override fun onResponse(
                call: retrofit2.Call<ResponseIsFake>,
                response: Response<ResponseIsFake>
            ) {
                result.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<ResponseIsFake>, t: Throwable) {
                throw Error(t.message.toString())
            }
        })
        return result
    }
     */
}