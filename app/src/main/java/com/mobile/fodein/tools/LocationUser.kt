package com.mobile.fodein.tools

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

class LocationUser @Inject constructor(activity: AppCompatActivity){
    var latitude = 0.0
    var longitude = 0.0
    private var locationManager : LocationManager? = null

    init {
        locationManager = activity.getSystemService(
                AppCompatActivity.LOCATION_SERVICE) as LocationManager?
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            longitude = location.longitude
            latitude = location.latitude
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun updateLocation(){
        try {
            locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L, 0f, locationListener)
        } catch(ex: SecurityException) {
            println(ex.message)
        }
    }




}