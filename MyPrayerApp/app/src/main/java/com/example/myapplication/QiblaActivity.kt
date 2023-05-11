package com.example.myapplication
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.runtime.invalidateGroupsWithKey
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.rotationMatrix
import com.google.android.material.bottomnavigation.BottomNavigationView
private lateinit var mSensorManager: SensorManager
private lateinit var mMagnetometer: Sensor
private lateinit var mAccelerometer: Sensor
private lateinit var mLocation: Location
private lateinit var mImageViewCompass: AppCompatImageView
private lateinit var mImageArrow: AppCompatImageView
private var mGravityValues = FloatArray(3)
private val mAccelerationValues = FloatArray(3)
private val mRotationMatrix = FloatArray(9)
private val orientation = FloatArray(3)
private var kabaahLatitude = 25.4106386
private  var kabaahLongitude =  -54.189238
private  var mAzimuth = 0f
class QiblaActivity : AppCompatActivity() {
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
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(mSensorListener,mMagnetometer,SensorManager.SENSOR_DELAY_FASTEST)
        mSensorManager.registerListener(mSensorListener,mAccelerometer,SensorManager.SENSOR_DELAY_FASTEST)
    }
    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(mSensorListener)
    }
}
       private val mSensorListener = object : SensorEventListener{
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor == null) {
                return
            }
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mAccelerationValues, 0, mAccelerationValues.size)
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mGravityValues, 0, mGravityValues.size)
            }
            val success = SensorManager.getRotationMatrix(
                mRotationMatrix,
                null,
                mAccelerationValues,
                mGravityValues
            )
            if (success) {
                SensorManager.getOrientation(mRotationMatrix, orientation)
                val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                val azimuthFix = (azimuth + 360) % 360
                val distance = mLocation.distanceTo(mLocation)
                val bearing = mLocation.bearingTo(mLocation)
                val direction = (azimuthFix + bearing) % 360
                val rotateAnimation = RotateAnimation(
                    mAzimuth,
                    -direction,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                rotateAnimation.duration = 1000
                rotateAnimation.fillAfter = true
                mImageViewCompass.startAnimation(rotateAnimation)
                mAzimuth = -direction
            }
        }
           override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
               //Nothing to do
           }
    }


