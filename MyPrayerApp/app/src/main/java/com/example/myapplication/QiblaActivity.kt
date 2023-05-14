package com.example.myapplication
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_qibla.*
import java.security.Permission


private lateinit var mSensorManager: SensorManager
private lateinit var mMagnetometer: Sensor
private lateinit var mAccelerometer: Sensor
private lateinit var mLocation: Location
private lateinit var mImageViewCompass: AppCompatImageView
private var mGravityValues = FloatArray(3)
private var mGeomagneticValues = FloatArray(3)
private var kabaahLatitude = 21.422487
private  var kabaahLongitude =  39.826206
private  var mAzimuth = 0f

private val PERMISSION_REQUEST_CODE = 1001

class QiblaActivity : AppCompatActivity(), SensorEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qibla)
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
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mImageViewCompass = findViewById(R.id.imgViewCompass)
        mLocation = Location(" ")
        mLocation.latitude = kabaahLatitude
        mLocation.longitude = kabaahLongitude
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
            } else {
                getLocation()
            }
        } else {
            getLocation()
        }
    }
    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this,mMagnetometer,SensorManager.SENSOR_DELAY_GAME)
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_GAME)
    }
    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent?) {
      if (event?.sensor == mMagnetometer){
          mGeomagneticValues = event.values
      }
        if (event?.sensor == mAccelerometer){
            mGravityValues = event.values
        }
        val R= FloatArray(9)
        val I =FloatArray(9)
        val success = SensorManager.getRotationMatrix(R,I, mGravityValues, mGeomagneticValues)
        if (success){
            val orientation = FloatArray(3)
            SensorManager.getOrientation(R,orientation)
            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            val rotateAnimation = RotateAnimation(mAzimuth, -azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
            rotateAnimation.duration = 400
            rotateAnimation.fillAfter = true
            mImageViewCompass.startAnimation(rotateAnimation)
            mAzimuth = -azimuth
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


    private fun getLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            val mLocation: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (mLocation !=null){
                 kabaahLatitude = mLocation.latitude
                 kabaahLongitude = mLocation.longitude
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] ==PackageManager.PERMISSION_GRANTED){getLocation()}
        }
    }
}






