package com.dani.kotlin.findbus.connection

import android.content.Context
import android.graphics.Color
import com.dani.kotlin.findbus.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class DirectionsService(private val context: Context) {

    private val client = OkHttpClient()
    private lateinit var polylineOptions: ArrayList<PolylineOptions>

    companion object {
        val DRIVING = "driving"
        val WALKING = "walking"
        val BICYCLING = "bicycling"
        val TRANSIT = "transit"
    }

    fun calculateRoute(pointA: LatLng, pointB: LatLng?, mode: String): ArrayList<PolylineOptions> {
        val pointAStr = pointA.latitude.toString()  + "," + pointA.longitude.toString()
        val pointBStr = pointB?.latitude.toString() + "," + pointB?.longitude.toString()
        val endpoint = context.getString(R.string.maps_directions_api,
            context.getString(R.string.maps_directions_parameters,
            pointAStr, pointBStr, mode, context.getString(R.string.google_maps_json_key)))

        val request = Request.Builder().header(
            "Referer", "com.dani.kotlin.findbus")
            .url(endpoint).build()

        // TODO: Investigate how to await the json response
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()
                polylineOptions = parseResponse(responseString)
            }
        })

        // TODO: Remove this ASAP! No sleep for you!
        Thread.sleep(750)
        return polylineOptions
    }

    private fun parseResponse(response: String?): ArrayList<PolylineOptions> {
        val jsonResponse = JSONObject(response)
        val routes = jsonResponse.getJSONArray("routes")
        val legs = routes.getJSONObject(0).getJSONArray("legs")
        val steps = legs.getJSONObject(0).getJSONArray("steps")

        val path: MutableList<List<LatLng>> = ArrayList()
        val polylines = arrayListOf<PolylineOptions>()

        for (i in 0 until steps.length()) {
            val points = steps.getJSONObject(i)
                .getJSONObject("polyline")
                .getString("points")

            path.add(PolyUtil.decode(points))
        }

        for (i in 0 until path.size) {
            val polylineOptions = PolylineOptions()
                .addAll(path[i]).color(Color.RED)
            polylines.add(polylineOptions)
        }

        return polylines
    }
}