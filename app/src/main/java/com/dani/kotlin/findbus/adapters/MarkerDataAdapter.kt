package com.dani.kotlin.findbus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.dani.kotlin.findbus.MapsActivity
import com.dani.kotlin.findbus.R
import com.dani.kotlin.findbus.connection.SoapConnector
import com.dani.kotlin.findbus.models.Arrive
import com.dani.kotlin.findbus.models.Arrives
import com.dani.kotlin.findbus.models.Stops
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerDataAdapter(private val context: Context)
    : GoogleMap.InfoWindowAdapter
{
    private val arrives: List<Arrives>
    val stops: Stops

    init {
        val connector = SoapConnector(context)
        stops = connector.getStopsFromXY(
            MapsActivity.userLocation.longitude,
            MapsActivity.userLocation.latitude,
            400.0)

        arrives = mutableListOf()
        for (stop in stops.elements) {
            arrives.add(connector.getArriveStop(stop.idStop))
        }
    }

    override fun getInfoContents(marker: Marker?): View {
        val markerView = LayoutInflater.from(context).inflate(
            R.layout.marker_info_window, null)

        val layout = markerView.findViewById<LinearLayout>(R.id.dataLayout)
        val name = markerView.findViewById<TextView>(R.id.name)
        val stop = markerView.findViewById<TextView>(R.id.idStopValue)
        val street = markerView.findViewById<TextView>(R.id.streetValue)
        val stopLines = markerView.findViewById<TextView>(R.id.idStopLinesValue)

        val index = marker?.tag as Int
        name.text = stops.elements[index].name
        stop.text = stops.elements[index].idStop
        street.text = stops.elements[index].postalAdress
        stopLines.text = getStopLinesString(index)

        for (arrive in arrives[index].elements)
            layout.addView(buildChildTextView(arrive))

        return markerView
    }

    private fun buildChildTextView(arrive: Arrive): TextView {
        val textView = TextView(context)
        textView.setPadding(8,
            0 ,0 ,0)

        val timeLeft = when (arrive.getTimeArrival()) {
            context.getString(R.string.marker_window_bus_waiting) -> arrive.getTimeArrival()
            else -> context.getString(R.string.marker_window_arrival_in) + arrive.getTimeArrival()
        }

        textView.text = context.getString(
            R.string.marker_window_line, arrive.idLine,
            arrive.destination, timeLeft)
        return textView
    }

    private fun getStopLinesString(index: Int): String
    {
        val lines = mutableListOf<Arrive>()
        lines.addAll(arrives[index].elements)
        lines.sortedBy { it.idLine }

        var linesString = lines.elementAt(0).idLine + ","
        for (line in lines) {
            val idLine = " " + line.idLine + ","
            if (linesString.indexOf(line.idLine) == -1)
                linesString += idLine
        }

        linesString = linesString.substring(
            0, linesString.length - 1)

        return linesString
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }
}