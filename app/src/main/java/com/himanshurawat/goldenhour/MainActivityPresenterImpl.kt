package com.himanshurawat.goldenhour

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import org.shredzone.commons.suncalc.MoonTimes
import org.shredzone.commons.suncalc.SunTimes
import java.text.SimpleDateFormat
import java.util.*


class MainActivityPresenterImpl(private val view: MainActivityContract.View): MainActivityContract.Presenter {
    override fun forwardDate(latLng: LatLng) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH,1)
        val newDate = calendar.time
        calulatePhaseTime(latLng, newDate)
    }

    override fun rewindDate(latLng: LatLng) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH,-1)
        val newDate = calendar.time
        calulatePhaseTime(latLng, newDate)
    }


    override fun saveMarkerPosition(latLng: LatLng) {
        model.saveMarker(latLng.latitude,latLng.longitude)
    }

    override fun setPreviousMarker() {
        val latLng = model.reteriveMarker()
        if (latLng != null){
            view.moveToMarker(latLng)

            calulatePhaseTime(latLng,Date())
        }else{
            view.hidePhaseTime()
        }

    }

    private lateinit var model: MainActivityModelImpl
    private var date: Date? = null

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


    override fun calulatePhaseTime(latLng: LatLng, date: Date) {
        this.date = date
        val dateFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        //Sun
        val sunTime: SunTimes = SunTimes.compute().on(date).at(latLng.latitude,latLng.longitude).execute()
        val sunRise = sunTime.rise
        val sunRiseString = dateFormatter.format(sunRise)
        val sunSet = sunTime.set
        val sunSetString = dateFormatter.format(sunSet)

        //Moon
        val moonTime: MoonTimes = MoonTimes.compute().on(date).at(latLng.latitude,latLng.longitude).execute()
        val moonRise = moonTime.rise
        val moonRiseString = if(moonRise != null)dateFormatter.format(moonRise)else "--:--"
        val moonSet = moonTime.set
        val moonSetString = if(moonSet != null)dateFormatter.format(moonSet) else "--:--"

        val secondDateFormatter = SimpleDateFormat("E, dd-MMM-yyyy",Locale.getDefault())
        val dateString = secondDateFormatter.format(date)

        //Setting Views
        view.setTextView(sunRiseString,sunSetString,moonRiseString,moonSetString,dateString)
        view.showPhaseTime()
    }






}