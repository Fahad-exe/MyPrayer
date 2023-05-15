package com.example.myapplication

import com.example.myapplication.model.Solat
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("{city}.json?key=57170c1d516c5a26eadb1f4eb67feb89")
    fun getApi(@Path("city") city: String) :Call<Solat>


    companion object{
        fun create() : RetrofitService{
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://muslimsalat.com").build()
            return retrofit.create(RetrofitService::class.java)
        }
    }
}