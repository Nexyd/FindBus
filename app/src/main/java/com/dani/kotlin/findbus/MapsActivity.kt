package com.dani.kotlin.findbus

import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.dani.kotlin.findbus.adapters.MarkerDataAdapter
import com.dani.kotlin.findbus.models.Stop
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.disposables.CompositeDisposable

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener
{
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var data: MarkerDataAdapter

    companion object {
        var userLocation = LatLng(0.0, 0.0)
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        val COMPOSITE_DISPOSABLE = CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.
            findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        COMPOSITE_DISPOSABLE.dispose()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        // TODO: Calculate route from user to mark
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, 17f))
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
        data = MarkerDataAdapter(this.baseContext)

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.setInfoWindowAdapter(data)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
        {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    userLocation = LatLng(location.latitude, location.longitude)
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))

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

            placeMarkersInMap(position, stop, i)
        }
    }

    private fun placeMarkersInMap(
        position: LatLng, stop: Stop, index: Int)
    {
        // TODO: Add line breaks to the InfoWindow
        // TODO: (Optional) Add custom icon to mark
        val markerOptions = MarkerOptions()
            .position(position)
            .title(stop.name)
//            .snippet(buildSnippetData(stop, arrives))
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