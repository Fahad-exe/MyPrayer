package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.runtime.invalidateGroupsWithKey
import androidx.core.content.ContextCompat.getSystemService

private lateinit var mSensorManager: SensorManager
private lateinit var mMagnetometer: Sensor
private lateinit var mAccelerometer: Sensor
private lateinit var mImageViewCompass: AppCompatImageView
private var mGravityValues = FloatArray(3)
private val mAccelerationValues = FloatArray(3)
private val mRotationMatrix = FloatArray(9)
private var mLastDirectionInDegrees = 0f


class QiblaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qibla)
        mImageViewCompass = findViewById(R.id.imgViewCompass)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }
    override fun onResume(){
        super.onResume()
        mSensorManager.registerListener(mSensorListener,mMagnetometer,SensorManager.SENSOR_DELAY_FASTEST)
        mSensorManager.registerListener(mSensorListener,mAccelerometer,SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(mSensorListener)
    }
}

private val mSensorListener = object : SensorEventListener {


    override fun onSensorChanged(event: SensorEvent) {
        calculateCompassDirection(event)
    }

    private fun calculateCompassDirection(event: SensorEvent) {

        when (event.sensor.type){
            Sensor.TYPE_MAGNETIC_FIELD -> mGravityValues = event.values.clone()}
        val success = SensorManager.getRotationMatrix(mRotationMatrix,null,mAccelerationValues,mGravityValues)
        if (success){
            val orientationValues = FloatArray(3)
            SensorManager.getOrientation(mRotationMatrix,orientationValues)
            val azimuth = Math.toDegrees(-orientationValues[0].toDouble()).toFloat()
            val rotateAnimation = RotateAnimation(mLastDirectionInDegrees,azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
            rotateAnimation.duration = 50
            rotateAnimation.fillAfter = true
            mImageViewCompass!!.startAnimation(rotateAnimation)
            mLastDirectionInDegrees = azimuth
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //Nothing to do
    }


}

