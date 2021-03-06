package com.dani.kotlin.findbus

import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.dani.kotlin.findbus.adapters.MarkerDataAdapter
import com.dani.kotlin.findbus.connection.DirectionsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener
{
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var data: MarkerDataAdapter
    private lateinit var directions: DirectionsService
    private lateinit var mapPolylines: MutableList<Polyline>

    companion object {
        var userLocation = LatLng(0.0, 0.0)
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        checkPermission()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.
            findFragmentById(R.id.map) as SupportMapFragment

        mapPolylines = mutableListOf()
        directions = DirectionsService(baseContext)
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(this)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)

            // TODO: Prevent the exception occuring when the map has not been initialized
            //if (::map.isInitialized)
                map.clear()
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        for (polyline in mapPolylines)
            polyline.remove()

        // TODO: Call calculateRoute() asynchronously
        val options = directions.calculateRoute(
            userLocation, marker?.position,
            DirectionsService.WALKING)

        for (option in options)
            mapPolylines.add(map.addPolyline(option))

        map.animateCamera(CameraUpdateFactory
            .newLatLngZoom(marker?.position, 17f))

        return false
    }

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
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        getUserLocation()
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
        {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    userLocation = LatLng(
                        location.latitude,
                        location.longitude)

                    data = MarkerDataAdapter(this.baseContext)
                    map.setInfoWindowAdapter(data)
                    map.animateCamera(CameraUpdateFactory
                        .newLatLngZoom(userLocation, 17f))

                    getDataForMarkers()
                }
            }
        }
    }

    private fun getDataForMarkers() {
        for (i in 0 until data.stops.elements.size) {
            val stop = data.stops.elements[i]
            val position = LatLng(
                stop.coordinateY.toDouble(),
                stop.coordinateX.toDouble())

            placeMarkersInMap(position, i)
        }
    }

    private fun placeMarkersInMap(
        position: LatLng, index: Int)
    {
        // TODO: (Optional) Add custom icon to mark
        val markerOptions = MarkerOptions()
            .position(position)
//            .icon(BitmapDescriptorFactory.fromBitmap(
//                 BitmapFactory.decodeResource(resources,
//                 R.mipmap.ic_user_location)))

        map.addMarker(markerOptions).tag = index
    }

    /*private fun getAddress(latLng: LatLng): String {
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
    } */
}