package com.example.myapplication


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class CompassQiblaViewModel: ViewModel() {

    private val _locationAddress = MutableLiveData<Address>()
    val locationAddress get() = _locationAddress

    private val _direction = MutableLiveData<QiblaDirection>()
    val direction get() = _direction

    private val _permission = MutableLiveData<Pair<Boolean, String>>()
    val permission get() = _permission

    fun updateCompassDirection(qiblaDirection: QiblaDirection){
        viewModelScope.launch {
            _direction.value = qiblaDirection
        }
    }

    fun getLocationAddress(context: Context){
        viewModelScope.launch {
            Geocoder(context, Locale.getDefault()).apply {
                getFromLocation(50.3755, 4.1427, 1).first()
                    .let { address ->
                        _locationAddress.value = address
                    }
            }
        }
    }

    fun onPermissionUpdate(isGranted: Boolean, message: String? = ""){
        _permission.value = Pair(isGranted, message?:"")
    }
}