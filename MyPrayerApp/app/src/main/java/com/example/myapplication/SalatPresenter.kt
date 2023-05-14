package com.example.myapplication

import android.content.Context
import com.example.myapplication.model.Item
import com.example.myapplication.model.Solat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalatPresenter(context: Context) {
    val isalatView = context as SalatView

    fun getDataFromApi(city:String){
        RetrofitService.create().getApi(city).enqueue(object :Callback<Solat>{
            override fun onResponse(call: Call<Solat>, response: Response<Solat>) {
                isalatView.onDataCompleteFromApi(response.body()?.items?.get(0) as Item)
            }

            override fun onFailure(call: Call<Solat>, t: Throwable) {
                isalatView.onDataErrorFromApi(t)
            }
        })
    }
}