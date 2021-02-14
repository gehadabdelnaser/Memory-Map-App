package com.gehad.memorymap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.gehad.base.BaseActivity
import com.gehad.memorymap.dataBase.MyDataBase
import com.gehad.memorymap.dataBase.model.Note
import com.gehad.memorymap.pojo.Constant
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : BaseActivity(), LocationListener, OnMapReadyCallback
    ,GoogleMap.OnMapClickListener,GoogleMap.OnInfoWindowClickListener{

    private var userLocation: Location?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        if(isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            // granted // call your function
            showUserLocation()
        } else{
            requestPermissionIfAvailable()
        }
        // to show may when it ready
        //this is object from interface OnMapReadyCallback
        mapFragment.getMapAsync(this)
        updateUserMarker()
    }

    private fun requestPermissionIfAvailable() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            showMassage(R.string.warning,
                R.string.location_permission_explanation_massage,
                R.string.yes,
                { _, _ ->
                    requestLocationPermission()
                },
                R.string.cancel,
                { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }, true)

        }
        // if first request permission
        else requestLocationPermission()
    }

    private fun requestLocationPermission() {

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Constant.ACCESS_LOCATION_REQUEST_CODE)
    }

    private lateinit var myLocationProvider: MyLocationProvider

    private fun showUserLocation() {

        Toast.makeText(this,"Permission granted..show location.",
            Toast.LENGTH_LONG).show()

        myLocationProvider = MyLocationProvider(activity)

        //this is object from interface LocationListener
        userLocation = myLocationProvider.getLocation(this)
        Log.e("Location" , "${userLocation?.latitude}  ${userLocation?.longitude}")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {

        if(requestCode == Constant.ACCESS_LOCATION_REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                showUserLocation()
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this,"user refused our request ", Toast.LENGTH_LONG)
                    .show()
            }
        }
        return
    }

    // 4 methods of interface LocationListener

    override fun onLocationChanged(location: Location?) {
        userLocation =location
        updateUserMarker()
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {

    }

    private var googleMap:GoogleMap?=null
    private var userMarker:Marker?=null
    // method of interface OnMapReadyCallback
    //nullable because there is no internet or problem in location

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        if(userLocation != null) {
            updateUserMarker()
        }
        addMarker()
        googleMap?.setOnMapClickListener(this)
        googleMap?.setOnInfoWindowClickListener(this)

    }

    override fun onMapClick(location: LatLng?) {
        val intent= Intent(activity, AddNoteActivity::class.java)
        if (location != null) {
            intent.putExtra(Constant.LATITUDE, location.latitude)
            intent.putExtra(Constant.LONGITUDE, location.longitude)
            startActivity(intent)
        }
    }

    override fun onInfoWindowClick(p0 : Marker?) {
        val location=p0?.position
        val intent= Intent(activity, NoteDetailsActivity::class.java)
        if (location != null) {
            intent.putExtra(Constant.LATITUDE, location.latitude)
            intent.putExtra(Constant.LONGITUDE, location.longitude)
            startActivity(intent)
        }
    }

    private fun getAllNotes():MutableList<Note>?{
        val data= MyDataBase.getInstance(applicationContext)
            ?.notesDao()
            ?.getAllNotes()
        return (data?.toMutableList())
    }

    private fun addMarker(){
        val list=getAllNotes()!!.iterator()
        for(note in list){
            val marker=MarkerOptions().position(LatLng(note.latitude
                ,note.longitude))
                .title(note.title)
            googleMap?.addMarker(marker)
        }
    }

    private fun updateUserMarker() {

        val marker = MarkerOptions()
                .position(LatLng(userLocation!!.longitude, userLocation!!.longitude))
                .title("your location")

        // to update position of marker
        //if it is not exist (i.e) -> the first time it appears
        if (userMarker == null)
            userMarker = googleMap?.addMarker(marker)
        else {
            userMarker?.position = LatLng(userLocation!!.longitude, userLocation!!.longitude)
        }

        // to foucs on location use animateCamera
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLocation!!.longitude, userLocation!!.longitude), 12f
        ))

    }

    override fun onStart() {
        super.onStart()
        addMarker()
    }
}