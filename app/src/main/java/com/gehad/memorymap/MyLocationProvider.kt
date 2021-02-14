package com.gehad.memorymap

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.gehad.memorymap.pojo.Constant

class MyLocationProvider {

    var locationManger:LocationManager

    constructor(context:Context){
        locationManger = context
            .getSystemService(Context.LOCATION_SERVICE)as LocationManager
    }

    @SuppressLint("MissingPermission")
    fun getLocation(locationListener: LocationListener?): Location? {

        val gpsEnable=locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnable=locationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if(!gpsEnable && !networkEnable)
            return null

        val provider=if(gpsEnable)
            LocationManager.GPS_PROVIDER
        else
            LocationManager.NETWORK_PROVIDER

        // fun to get last known location by provider -> Gps or Network
        var location=locationManger.getLastKnownLocation(provider)


        //if you want update location
        if (locationListener != null){
            locationManger.requestLocationUpdates(provider,
                Constant.MIN_TIME_BETWEEN_UPDATES,Constant.MIN_DISTANCE_BETWEEN_UPDATES,
                locationListener)
        }

        // if my location is null
        if(location==null){
            //get my last location by any providers
            location = getBastLastKnownLocation()
        }
        return location
    }
    // if you put location permission in app but if else you must add location permission

    @SuppressLint("MissingPermission")
    private fun getBastLastKnownLocation(): Location? {
        val providers=locationManger.allProviders
        var bestLocation:Location?=null

        for(p in providers){
            val location=locationManger.getLastKnownLocation(p)

            if (bestLocation ==null) bestLocation=location

            if (location == null || bestLocation == null) continue

            if (location.time > bestLocation.time)
                bestLocation = location

        }
        return bestLocation
    }

}