package com.example.myapplication
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Address
import android.location.Location
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.LocationUtils.checkLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CompassQibla {

    class Builder(private val activity: AppCompatActivity) : SensorEventListener {

        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var currentLocation: Location
        private lateinit var sensorManager: SensorManager
        private lateinit var sensor: Sensor
        private lateinit var mMag: Sensor
        private var currentDegree = 0f
        private var currentDegreeNeedle = 0f
        private val model: CompassQiblaViewModel =
            ViewModelProvider(activity).get(CompassQiblaViewModel::class.java)

        fun build() {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            activity.checkLocationPermission { requestLocationPermission() }
            model.getLocationAddress(activity)
            sensorManager =
                activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            sensorManager.registerListener(
                this, sensor, SensorManager.SENSOR_DELAY_GAME
            )
//            fusedLocationClient.lastLocation.addOnSuccessListener {
//                it?.let { location ->
////                    Toast.makeText(activity, ""+ location.latitude.toString(), Toast.LENGTH_SHORT).show()
//                    currentLocation = location
//                    model.getLocationAddress(activity)
//                    sensorManager =
//                        activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
//                    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
//                    sensorManager.registerListener(
//                        this, sensor, SensorManager.SENSOR_DELAY_GAME
//                    )
//                }
//            }
        }

        fun onPermissionGranted(onGranted: (permission: String) -> Unit) = apply {
            model.permission.observe(activity) { if (it.first) onGranted(it.second) }
        }

        fun onPermissionDenied(onDenied: () -> Unit) = apply {
            model.permission.observe(activity) { if (!it.first) onDenied() }
        }

        fun onDirectionChangeListener(onChange: (qiblaDirection: QiblaDirection) -> Unit) = apply {
            model.direction.observe(activity) { onChange(it) }
        }

        fun onGetLocationAddress(onGetLocation: (address: Address) -> Unit) = apply {
            model.locationAddress.observe(activity) { onGetLocation(it) }
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event?.values)
            val orientationValues = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationValues)
            val degree = Math.toDegrees(orientationValues[0].toDouble()).toFloat()

            val destinationLoc = Location("service Provider").apply {
                latitude = 21.422487
                longitude = 39.826206
            }
            currentLocation = Location("provider")
            currentLocation?.latitude = 50.3755
            currentLocation?.longitude =4.1427
            var bearTo: Float = currentLocation.bearingTo(destinationLoc)
            if (bearTo < 0) bearTo += 360
            var direction: Float = bearTo - degree
            if (direction < 0) direction += 360

            val isFacingQibla = direction in 359.0..360.0 || direction in 0.0..1.0


            currentDegreeNeedle = direction
            currentDegree = -degree
            val qiblaDirection = QiblaDirection(-degree, direction, isFacingQibla)
            model.updateCompassDirection(qiblaDirection)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        @SuppressLint("NewApi")
        fun requestLocationPermission() {
            val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
            val permissionGranted = PackageManager.PERMISSION_GRANTED
            val hasPermission = ContextCompat.checkSelfPermission(activity, locationPermission) == permissionGranted

            if (hasPermission) {
                model.onPermissionUpdate(true, locationPermission)
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(locationPermission),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        companion object {
            private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        }
    }
}
