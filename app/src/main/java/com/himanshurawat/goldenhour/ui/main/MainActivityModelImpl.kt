package com.himanshurawat.goldenhour.ui.main

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.himanshurawat.goldenhour.db.ItemDatabase
import com.himanshurawat.goldenhour.db.entity.Item

class MainActivityModelImpl(context: Context):
    MainActivityContract.Model {

    override fun addItem(item: Item) {
        database.getDao().insert(item)
    }

    override fun saveMarker(lat: Double,long: Double) {
        pref.edit().putString("lat",lat.toString()).apply()
        pref.edit().putString("long",long.toString()).apply()
    }

    override fun retreiveMarker(): LatLng? {
        val lat = pref.getString("lat","0.0") as String
        val long = pref.getString("long","0.0") as String
        if(lat.equals("0.0")&&long.equals("0.0")){
            return null
        }
        return LatLng(lat.toDouble(),long.toDouble())
    }

    private val pref = context.applicationContext.getSharedPreferences("userPref",Context.MODE_PRIVATE)
    private val database = ItemDatabase.getInstance(context)




}