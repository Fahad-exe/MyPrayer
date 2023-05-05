package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                R.id.Time_ic -> {
                    // Handle the settings action
                    startActivity(Intent(this, TimeActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}

