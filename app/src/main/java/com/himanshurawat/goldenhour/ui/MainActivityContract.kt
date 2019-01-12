package com.himanshurawat.goldenhour.ui

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import java.util.*

interface MainActivityContract {

    interface Model{
        fun saveMarker(lat: Double,long: Double)
        fun reteriveMarker(): LatLng?
    }

    interface View{
        fun requestLocationDialog()
        fun requestLocationPermission()
        fun moveToMarker(latLng: LatLng)
        fun setTextView(sunRise: String, sunSet: String, moonRise: String, moonSet: String,dateString: String)
        fun showPhaseTime()
        fun hidePhaseTime()
    }

    interface Presenter{
        fun setupModel(context: Context)
        fun checkLocationPermission(context: Context)
        fun dialogConfirm()
        fun dialogCancel()
        fun setPreviousMarker()
        fun saveMarkerPosition(latLng: LatLng)
        fun calulatePhaseTime(latLng: LatLng,date: Date)
        fun forwardDate(latLng: LatLng)
        fun rewindDate(latLng: LatLng)
        fun setNotification(context: Context)


    }
}