package com.himanshurawat.goldenhour

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import java.util.jar.Manifest

class MainActivityPresenterImpl(private val view: MainActivityContract.View): MainActivityContract.Presenter {
    override fun saveMarkerPosition(latLng: LatLng) {
        model.saveMarker(latLng.latitude,latLng.longitude)
    }

    override fun setPreviousMarker() {
        val latLng = model.reteriveMarker()
        if (latLng != null){
            view.moveToMarker(latLng)
        }
    }

    private lateinit var model: MainActivityModelImpl

    override fun setupModel(context: Context) {
        model = MainActivityModelImpl(context)
    }


    override fun dialogCancel() {

    }

    override fun dialogConfirm() {
        view.requestLocationPermission()
    }




    override fun checkLocationPermission(context: Context) {
        //Using Application Context to Avoid Mem Leaking
        if(ContextCompat.
                checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.
                checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Location Permission Not Granted
            view.requestLocationDialog()
        }else{
            //Permission Granted
        }
    }




}