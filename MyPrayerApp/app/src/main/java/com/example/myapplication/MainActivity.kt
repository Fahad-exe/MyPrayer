package com.example.myapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val sliderview = findViewById<ImageSlider>(R.id.image_slider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/say-allahu-akbar-to-start-salah-2.jpg","Starting the Prayer"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/standing-in-salah-recite-surah-fatihah.jpg","Prayer stand "))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/how-to-make-ruku-bowing-down.jpg","Ruku/Bowing Down"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/making-sujud-in-salah.jpg","Sujud/Prostration"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/make-sujud-in-salah.jpg","The Rear View of Sujud/Prostration"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/sitting-between-sujud.jpg","Sitting between sujud/Prostration"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/sitting-in-salah-and-tashahud.jpg","Rear View"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/sitting-position-in-salah.jpg","preform Tashahud/"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/end-the-salah-with-tasleem-.jpg","Tasleem/Ending the prayer"))
        imageList.add(SlideModel("https://www.mymasjid.ca/wp-content/uploads/2017/03/end-the-salah-with-tasleem-2.jpg","Final Step"))

        sliderview.setImageList(imageList,ScaleTypes.FIT)
        bottomNavigationView.setOnItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.Qibla_ic -> {
                    // Handle the Qibla action
                    startActivity(Intent(this, QiblaActivity::class.java))

                    true
                }
                R.id.Quran_ic -> {
                    // Handle the Quran action
                    startActivity(Intent(this, QuranActivity::class.java))
                    true
                }
                R.id.Time_ic -> {
                    // Handle the Time action
                    startActivity(Intent(this, TimeActivity::class.java))
                    true
                }

                else -> false
            }

        }
    }
}

