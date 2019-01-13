package com.himanshurawat.goldenhour.ui.main

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.himanshurawat.goldenhour.R
import com.himanshurawat.goldenhour.broadcastreceiver.GoldenHourBroadcast
import com.himanshurawat.goldenhour.ui.saved.SavedActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    MainActivityContract.View{


    override fun cancelNotification() {
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext,GoldenHourBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext,57,intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }


    override fun clearMap() {
        if(::mMap.isInitialized){
            mMap.clear()
        }
    }


    companion object {
        val LOCATION_REQUEST_CODE = 9
        val PLACES_REQUEST_CODE = 69
        val SAVED_ACTIVITY_REQUEST_CODE = 399
    }


    private var latLng: LatLng? = null
    private lateinit var mMap: GoogleMap
    private lateinit var presenter: MainActivityContract.Presenter
    private var isMarkerPlaced = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter = MainActivityPresenterImpl(this)
        presenter.setupModel(this)
        presenter.checkLocationPermission(this)
        setup()
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //BlueDot
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        }

        presenter.setPreviousMarker()

        mMap.setOnMapLongClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title("Marker"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it,15f))
            presenter.saveMarkerPosition(it)
            presenter.calculatePhaseTime(it, Date())
            presenter.setNotification(this)
            latLng = it
            isMarkerPlaced = true
            invalidateOptionsMenu()
        }

    }

    private fun setup(){
        activity_main_play_image_button.setOnClickListener {
            if(latLng != null){
                presenter.calculatePhaseTime(latLng as LatLng ,Date())
                presenter.setNotification(this)
            }
        }

        activity_main_forward_image_button.setOnClickListener {
            if(latLng != null){
                presenter.forwardDate(latLng as LatLng)
                presenter.setNotification(this)
            }
        }

        activity_main_rewind_image_button.setOnClickListener {
            if(latLng != null){
                presenter.rewindDate(latLng as LatLng)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.main_menu_search -> {
                try {
                    val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .build(this)

                    startActivityForResult(intent,
                        PLACES_REQUEST_CODE
                    )
                }catch (e: GooglePlayServicesRepairableException){

                } catch (e: GooglePlayServicesNotAvailableException){

                }
            }

            R.id.main_menu_clear_marker -> {
                presenter.clearMarker()
                isMarkerPlaced = false

                invalidateOptionsMenu()
                Snackbar.make(activity_main_root,"Marker Cleared",Snackbar.LENGTH_SHORT).show()
            }

            R.id.main_menu_save_marker ->{
                if(latLng != null){
                    presenter.saveItem(latLng as LatLng)
                    Snackbar.make(activity_main_root,"Current Marker Saved",Snackbar.LENGTH_SHORT).show()
                }

            }

            R.id.main_menu_view_saved_marker ->{
                val intent = Intent(this@MainActivity,SavedActivity::class.java)
                startActivityForResult(intent,SAVED_ACTIVITY_REQUEST_CODE)
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        if(isMarkerPlaced){
            menu.findItem(R.id.main_menu_search).isVisible = true
            menu.findItem(R.id.main_menu_clear_marker).isVisible = true
            menu.findItem(R.id.main_menu_save_marker).isVisible = true
            menu.findItem(R.id.main_menu_view_saved_marker).isVisible = true
        }else{
            menu.findItem(R.id.main_menu_search).isVisible = true
            menu.findItem(R.id.main_menu_clear_marker).isVisible = false
            menu.findItem(R.id.main_menu_save_marker).isVisible = false
            menu.findItem(R.id.main_menu_view_saved_marker).isVisible = true
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PLACES_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val place = PlaceAutocomplete.getPlace(this,data)
            val latLng = place.latLng
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng).title(place.name.toString()))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
            presenter.saveMarkerPosition(latLng)
            presenter.calculatePhaseTime(latLng, Date())
            presenter.setNotification(this)
            this.latLng = latLng
        }else if(requestCode == SAVED_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data != null){
                val latLng = LatLng(data.getDoubleExtra("lat",0.0),data.getDoubleExtra("lng",0.0))
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(latLng).title("Marker"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
                presenter.saveMarkerPosition(latLng)
                presenter.calculatePhaseTime(latLng, Date())
                presenter.setNotification(this)
                this.latLng = latLng
                isMarkerPlaced = true
                invalidateOptionsMenu()

            }

        }
    }

    override fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            LOCATION_REQUEST_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission Granted
                    if(::mMap.isInitialized){
                        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            mMap.isMyLocationEnabled = true
                        }
                    }
                }
            }
        }
    }


    override fun requestLocationDialog() {

        alert{
            title = "Allow Location Permission"
            message = "Location is an essential feature for this Application. We will be able to serve you"+
                    " better if you grant us Location Permission."
            noButton {
                title = "Dismiss"
                presenter.dialogCancel()
            }
            yesButton {
                presenter.dialogConfirm()
            }
        }.show()
    }


    override fun showPhaseTime() {
        activity_main_linear_layout.visibility = View.VISIBLE
    }

    override fun hidePhaseTime() {
        activity_main_linear_layout.visibility = View.GONE
    }

    override fun setTextView(sunRise: String, sunSet: String, moonRise: String, moonSet: String,dateString: String) {
        activity_main_sun_rise_text_view.text = sunRise
        activity_main_sun_set_text_view.text = sunSet
        activity_main_moon_rise_text_view.text = moonRise
        activity_main_moon_set_text_view.text = moonSet
        activity_main_date_text_view.text = dateString
    }

    override fun moveToMarker(latLng: LatLng) {
        isMarkerPlaced = true
        invalidateOptionsMenu()
        this.latLng = latLng
        mMap.addMarker(MarkerOptions().position(latLng).title("Last Saved Marker"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
    }

}
