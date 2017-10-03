package com.mobile.fodein.presentation.view.component

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.presentation.BaseActivity
import com.mobile.fodein.tools.PermissionUtils
import javax.inject.Inject


class ManageMaps @Inject constructor(
        private val activity: BaseActivity):
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private var googleMap: GoogleMap? = null
    private val permissionUtils: PermissionUtils = PermissionUtils()
    private val context = App.appComponent.context()
    val LOCATION_PERMISSION_REQUEST_CODE = 1
    var location: Location? = null
    var mapFragment: MapFragment? = null

    init {
        mapFragment = activity.fragmentManager
                .findFragmentById(R.id.map) as MapFragment
    }

    fun getMapAsync(){
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap!!.setOnMyLocationButtonClickListener(this)
        googleMap!!.setOnMyLocationClickListener(this)
        enableMyLocation()
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        this.location = p0
        googleMap!!.addMarker(MarkerOptions()
                .position(LatLng(p0.latitude, p0.longitude))
                .title(context.getString(R.string.my_place)))
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when {
            requestCode != LOCATION_PERMISSION_REQUEST_CODE -> return
            permissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION) -> enableMyLocation()
            else -> activity.toast(context.getString(R.string.access_not_allowed))
        }
    }

    private fun enableMyLocation() {
        when {
            ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED -> permissionUtils.requestPermission(activity,
                    LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            googleMap != null -> googleMap!!.isMyLocationEnabled = true
        }
    }

    private fun Activity.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}