package com.example.jejakrempah.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jejakrempah.data.JejakRempahItem
import com.example.jejakrempah.retrofit.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailViewModel : ViewModel() {

    // LiveData untuk menyimpan hasil response API
    private val _rempahDetail = MutableLiveData<Result<JejakRempahItem>>()
    val rempahDetail: LiveData<Result<JejakRempahItem>> get() = _rempahDetail

    // Retrofit API service
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://spice-service-994040585883.asia-southeast2.run.app/") // Ganti dengan base URL API Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


    // Fungsi untuk mengambil detail rempah
    fun fetchRempahDetail(rempahId: Int) {
        // Melakukan request API pada thread background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Mendapatkan semua data rempah dari API
                val response = apiService.getAlldata().execute()

                if (response.isSuccessful) {
                    // Mencari rempah dengan ID yang sesuai
                    val rempah = response.body()?.find { it.no == rempahId }

                    // Mengupdate LiveData di thread utama
                    withContext(Dispatchers.Main) {
                        if (rempah != null) {
                            _rempahDetail.value = Result.success(rempah)
                        } else {
                            _rempahDetail.value = Result.failure(Exception("Rempah not found"))
                        }
                    }
                } else {
                    // Menangani error dari API
                    withContext(Dispatchers.Main) {
                        _rempahDetail.value = Result.failure(Exception("Failed to load data"))
                    }
                }
            } catch (e: Exception) {
                // Menangani exception jika terjadi kesalahan
                withContext(Dispatchers.Main) {
                    _rempahDetail.value = Result.failure(e)
                }
            }
        }
    }
}
