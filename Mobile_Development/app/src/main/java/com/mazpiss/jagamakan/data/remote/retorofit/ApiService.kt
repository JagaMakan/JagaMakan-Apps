package com.mazpiss.jagamakan.data.remote.retorofit


import com.mazpiss.jagamakan.data.remote.response.DetectResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/predict")
    fun detectImage(
        @Part file: MultipartBody.Part,
    ): Call<DetectResponse>
}
