package com.mazpiss.jagamakan.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mazpiss.jagamakan.data.remote.response.DetectResponse
import com.mazpiss.jagamakan.data.remote.retorofit.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetectViewModel: ViewModel() {
    private val _detectImage = MutableLiveData<File?>()
    val detectImage: LiveData<File?>
        get() = _detectImage

    fun setScannedImage(file: File?) {
        _detectImage.value = file
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun clearDetect() {
        _detectImage.value = null
    }

    fun detectImage(apiService: ApiService, label: String, value: Int): MutableLiveData<DetectResponse?> {
        val result = MutableLiveData<DetectResponse?>()

        val file = _detectImage.value
        if (file != null) {
            val filePart = MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            val labelJson = Gson().toJson(label)
            val labelPart = RequestBody.create("text/plain".toMediaTypeOrNull(), label)
            val valuePart = RequestBody.create("text/plain".toMediaTypeOrNull(), value.toString())

            apiService.detectImage(filePart).enqueue(object : Callback<DetectResponse> {
                override fun onResponse(
                    call: Call<DetectResponse>,
                    response: Response<DetectResponse>,
                ) {
                    if (response.isSuccessful) {
                        val detectedLabelItem = response.body()
                        result.value = detectedLabelItem
                    } else {
                        result.value = null
                    }
                }

                override fun onFailure(call: Call<DetectResponse>, t: Throwable) {
                    result.value = null
                }

            })
        } else {
            result.value = null
        }

        return result
    }
}