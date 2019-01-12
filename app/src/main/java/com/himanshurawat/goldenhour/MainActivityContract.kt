package com.himanshurawat.goldenhour

import android.content.Context
import com.google.android.gms.maps.model.LatLng

interface MainActivityContract {

    interface Model{
        fun saveMarker(lat: Double,long: Double)
        fun reteriveMarker(): LatLng?
    }

    interface View{
        fun requestLocationDialog()
        fun requestLocationPermission()
        fun moveToMarker(latLng: LatLng)
    }

    interface Presenter{
        fun setupModel(context: Context)
        fun checkLocationPermission(context: Context)
        fun dialogConfirm()
        fun dialogCancel()
        fun setPreviousMarker()
        fun saveMarkerPosition(latLng: LatLng)
    }
}