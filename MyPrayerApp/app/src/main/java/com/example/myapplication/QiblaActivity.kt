package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityQiblaBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class QiblaActivity : AppCompatActivity() {

private var currentCompassDegree = 0f
    private var currentNeedleDegree = 0f
    private lateinit var binding: ActivityQiblaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQiblaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.home_ic -> {
                    // Handle the home action
                    startActivity(Intent(this, MainActivity::class.java))
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

        CompassQibla.Builder(this@QiblaActivity).onPermissionGranted { permission ->
        }.onPermissionDenied {
            Toast.makeText(this, "onPermissionDenied", Toast.LENGTH_SHORT).show()
        }.onGetLocationAddress { address ->
            binding.tvLocation.text = buildString {
                append(address.locality)
                append(", ")
                append(address.subAdminArea)
            }
        }.onDirectionChangeListener { qiblaDirection ->
            binding.tvDirection.text = if (qiblaDirection.isFacingQibla) "You're Facing Qibla"
            else "${qiblaDirection.needleAngle.toInt()}°"

            val rotateCompass = RotateAnimation(
                currentCompassDegree, qiblaDirection.compassAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentCompassDegree = qiblaDirection.compassAngle

            binding.ivCompass.startAnimation(rotateCompass)

            val rotateNeedle = RotateAnimation(
                currentNeedleDegree, qiblaDirection.needleAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentNeedleDegree = qiblaDirection.needleAngle

            binding.ivNeedle.startAnimation(rotateNeedle)
        }.build()

    }

}







