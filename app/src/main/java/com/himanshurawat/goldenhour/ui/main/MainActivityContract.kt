package com.himanshurawat.goldenhour.ui.main

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.himanshurawat.goldenhour.db.entity.Item
import java.util.*

interface MainActivityContract {

    interface Model{
        fun saveMarker(lat: Double,long: Double)
        fun retreiveMarker(): LatLng?
        fun addItem(item: Item)
    }

    interface View{
        fun requestLocationDialog()
        fun requestLocationPermission()
        fun moveToMarker(latLng: LatLng)
        fun setTextView(sunRise: String, sunSet: String, moonRise: String, moonSet: String,dateString: String)
        fun showPhaseTime()
        fun hidePhaseTime()
        fun clearMap()
        fun cancelNotification()
    }

    interface Presenter{
        fun setupModel(context: Context)
        fun checkLocationPermission(context: Context)
        fun dialogConfirm()
        fun dialogCancel()
        //PhaseTime
        fun setPreviousMarker()
        fun saveMarkerPosition(latLng: LatLng)
        fun calculatePhaseTime(latLng: LatLng, date: Date)
        fun forwardDate(latLng: LatLng)
        fun rewindDate(latLng: LatLng)
        fun setNotification(context: Context)
        //MenuOptions
        fun clearMarker()
        //Database
        fun saveItem(latLng: LatLng)
    }
}