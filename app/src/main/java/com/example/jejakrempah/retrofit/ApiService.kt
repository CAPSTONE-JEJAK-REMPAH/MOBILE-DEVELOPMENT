package com.example.jejakrempah.retrofit

import com.example.jejakrempah.data.JejakRempahItem
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {

    @GET("data")
    fun getAlldata(): Call<List<JejakRempahItem>>



}