package com.example.myapplication


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val prayerTimeFragment = PrayerTimeFragment()
        val qiblaFragment = QiblaFragment()
        val quranFragment = QuranFragment()
        val settingsFragment = SettingsFragment()
        val bottom_navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        makeCurrentFragment(homeFragment)



        bottom_navigation.setOnItemSelectedListener() {
            when(it.itemId){
                R.id.Home_ic-> makeCurrentFragment(homeFragment)
                R.id.Time_ic -> makeCurrentFragment(prayerTimeFragment)
                R.id.Qibla_ic -> makeCurrentFragment(qiblaFragment)
                R.id.Quran_ic -> makeCurrentFragment(quranFragment)
                R.id.Settings_ic-> makeCurrentFragment(settingsFragment)
            }
            true
        }



    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}