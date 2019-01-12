package com.himanshurawat.goldenhour

import android.content.Context
import com.google.android.gms.maps.model.LatLng

class MainActivityModelImpl(context: Context): MainActivityContract.Model{

    override fun saveMarker(lat: Double,long: Double) {
        pref.edit().putString("lat",lat.toString()).apply()
        pref.edit().putString("long",long.toString()).apply()
    }

    override fun reteriveMarker(): LatLng? {
        val lat = pref.getString("lat","0.0") as String
        val long = pref.getString("long","0.0") as String
        if(lat.equals("0.0")&&long.equals("0.0")){
            return null
        }
        return LatLng(lat.toDouble(),long.toDouble())
    }

    private val pref = context.applicationContext.getSharedPreferences("userPref",Context.MODE_PRIVATE)



}