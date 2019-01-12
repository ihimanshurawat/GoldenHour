package com.himanshurawat.goldenhour

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), OnMapReadyCallback, MainActivityContract.View{

    override fun moveToMarker(latLng: LatLng) {
        mMap.addMarker(MarkerOptions().position(latLng).title("Last Saved Marker"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
    }


    private val LOCATION_REQUEST_CODE = 9
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
                }
            }
        }
    }

}
