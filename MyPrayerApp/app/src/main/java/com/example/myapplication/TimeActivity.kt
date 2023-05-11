package com.example.myapplication

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TimeActivity : AppCompatActivity() {

    var fajr:TextView?=null
    var Dhur:TextView?=null
    var Asr:TextView?=null
    var Maghrib:TextView?=null
    var Isha:TextView?=null
    var searchEditText:EditText?=null
    var searchBtn:ImageView?=null
    var simpleDateFormat = SimpleDateFormat("hh:mm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.Qibla_ic -> {
                    // Handle the home action
                    startActivity(Intent(this, QiblaActivity::class.java))
                    true
                }
                R.id.Quran_ic -> {
                    // Handle the profile action
                    startActivity(Intent(this, QuranActivity::class.java))
                    true
                }
                R.id.home_ic -> {
                    // Handle the settings action
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                else -> false
            }
        }

        fajr=findViewById(R.id.fajr)
        Dhur=findViewById(R.id.Dhur)
        Asr=findViewById(R.id.Asr)
        Maghrib=findViewById(R.id.Maghrib)
        Isha=findViewById(R.id.Isha)
        searchEditText=findViewById(R.id.Searchcity)
        searchBtn=findViewById(R.id.search_btn)
        searchBtn?.setOnClickListener{
            LoadPrayerTime()
        }

    }

    private fun LoadPrayerTime() {
        val geocoder=Geocoder(this)
        val addressList:kotlin.collections.List<Address>?
        try {
            addressList =geocoder.getFromLocationName(searchEditText?.text.toString(),5)
            if (addressList !=null){
                val doubleLat=addressList[0].latitude
                val doubleLong = addressList[0].longitude
                val queue= Volley.newRequestQueue(this)
                val url = " http://api.aladhan.com/v1/calendar?latitude="+doubleLat+"&longitude="+doubleLong+"&method=2"
                val JsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,Response.Listener { response ->

                    val JsonData:JSONArray= response.getJSONArray("data")
                    val timings=JsonData.getJSONObject(0)
                    val tim =timings.getJSONObject("timings")
                    fajr?.setText(tim.getString("Fajr"))
                    Dhur?.setText(tim.getString("Dhuhr"))
                    Asr?.setText(tim.getString("Asr"))
                    Maghrib?.setText(tim.getString("Maghrib"))
                    Isha?.setText(tim.getString("Isha"))
                },Response.ErrorListener { error->
                    Log.d("Error",error.message!! )
                })
                queue.add(JsonObjectRequest)
            }
        }catch (e :Exception){
            e.printStackTrace()
        }
    }


}