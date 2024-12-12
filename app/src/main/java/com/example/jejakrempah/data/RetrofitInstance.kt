package com.example.jejakrempah.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: SpicesApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://spice-service-994040585883.asia-southeast2.run.app/data/")  // Base URL yang diperbaiki
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpicesApi::class.java)
    }
}
