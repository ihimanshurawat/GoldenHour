package com.himanshurawat.goldenhour

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, MainActivityContract.View{



    private val LOCATION_REQUEST_CODE = 9
    private var latLng: LatLng? = null
    private lateinit var mMap: GoogleMap
    private lateinit var presenter: MainActivityContract.Presenter


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
            presenter.calulatePhaseTime(it, Date())
            latLng = it

        }
    }

    private fun setup(){
        activity_main_play_image_button.setOnClickListener {
            if(latLng != null){
                presenter.calulatePhaseTime(latLng as LatLng ,Date())
            }
        }

        activity_main_forward_image_button.setOnClickListener {
            if(latLng != null){
                presenter.forwardDate(latLng as LatLng)
            }
        }

        activity_main_rewind_image_button.setOnClickListener {
            if(latLng != null){
                presenter.rewindDate(latLng as LatLng)
            }
        }
    }



    override fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST_CODE)
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
        mMap.addMarker(MarkerOptions().position(latLng).title("Last Saved Marker"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
    }

}
