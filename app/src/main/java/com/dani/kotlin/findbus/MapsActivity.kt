package com.dani.kotlin.findbus

import android.content.pm.PackageManager
import android.location.Location
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.dani.kotlin.findbus.beans.Arrives
import com.dani.kotlin.findbus.beans.Stops
import com.dani.kotlin.findbus.connection.FindBusConnector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener
{
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean = false

    // override fun onMarkerClick(p0: Marker?): Boolean {
    //     TODO("not implemented")
    // }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // TODO("Search for the Kotlin synthetic properties usage")
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)

            return
        }
    }

    private fun setUpMap() {
        checkPermission()
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

                printMarkers(location.latitude, location.longitude)
            }
        }
    }

    private fun printMarkers(x: Double, y: Double) {
        val connector = FindBusConnector()
        val stops: Stops = connector.getStopsFromXY(x, y)

        for (stop in stops) {
            val arrives: Arrives = connector
                .getArriveStop(stop.idStop)

            placeMarkerOnMap(LatLng(
                stop.coordinateX.toDouble(),
                stop.coordinateY.toDouble()
            ), arrives)
        }
    }

    private fun placeMarkerOnMap(location: LatLng, stopData: Arrives) {
        val markerOptions = MarkerOptions().position(location)
        val titleStr = getAddress(location)
        markerOptions.title(titleStr)
        map.addMarker(markerOptions)

        // TODO: Create InfoWindow (Show bus/stop data)
        // TODO: Calculate route from user to mark
        // TODO: (Optional) Add custom icon to mark
        // TODO: Add click listener for this events

        // map.moveCamera(CameraUpdateFactory.newLatLng(location))
        // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
        //     BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            addresses = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1)

            if (addresses != null && !addresses.isEmpty()) {
                address = addresses[0]
                val index = address.maxAddressLineIndex
                val lineCount = if (index >= 0) index + 1 else index

                for (i in 0 until lineCount) {
                    addressText += if (i == 0) address.getAddressLine(i)
                        else "\n" + address.getAddressLine(i)
                }
            }

        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }
}
