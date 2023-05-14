package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.model.Item
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_time.*


class TimeActivity : AppCompatActivity(), SalatView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
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
        search_btn?.setOnClickListener{
            SalatPresenter(this).getDataFromApi(Searchcity.text.toString())
        }

    }

    override fun onDataCompleteFromApi(salat: Item) {
        Fajr.text = salat.fajr
        Dhuhr.text = salat.dhuhr
        Asr.text = salat.asr
        Maghrib.text = salat.maghrib
        Isha.text = salat.isha
    }

    override fun onDataErrorFromApi(throwable: Throwable) {
        error("error ------------> ${throwable.localizedMessage}")

    }
}


