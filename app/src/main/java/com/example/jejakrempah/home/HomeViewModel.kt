package com.example.jejakrempah.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jejakrempah.data.JejakRempahItem
import com.example.jejakrempah.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _eventResponse = MutableLiveData<List<JejakRempahItem>>()
    val eventResponse: LiveData<List<JejakRempahItem>> get() = _eventResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchActiveEvents() {
        if (_eventResponse.value != null) {
            return
        }

        _isLoading.value = true
        ApiConfig.getApiService().getAlldata().enqueue(object : Callback<List<JejakRempahItem>> {


            override fun onResponse(
                call: Call<List<JejakRempahItem>>,
                response: Response<List<JejakRempahItem>>
            ) {
                _isLoading.value = false
                when (response.code()) {
                    200 -> {
                        _eventResponse.value = response.body()
                    }
                    400 -> {
                        _errorMessage.value = "Permintaan tidak valid. Silakan periksa kembali input Anda."
                    }
                    401 -> {
                        _errorMessage.value = "Anda perlu login untuk mengakses sumber daya ini."
                    }
                    403 -> {
                        _errorMessage.value = "Akses ditolak. Anda tidak memiliki izin untuk mengakses halaman ini."
                    }
                    404 -> {
                        _errorMessage.value = "Halaman yang Anda cari tidak ditemukan."
                    }
                    500 -> {
                        _errorMessage.value = "Terjadi kesalahan pada server. Silakan coba lagi nanti."
                    }
                    else -> {
                        _errorMessage.value = "Kesalahan tidak diketahui. Kode status: ${response.code()}"
                    }
                }
            }

            override fun onFailure(call: Call<List<JejakRempahItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}