package com.example.jejakrempah.data


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpicesApi {
    @GET("api/detect")  // Endpoint untuk deteksi rempah
    fun detectSpice(
        @Query("imageUrl") imageUrl: String // Mengirimkan URL gambar
    ): Call<DetectionResult>
}


