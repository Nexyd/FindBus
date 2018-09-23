package com.dani.kotlin.findbus.connection

import com.dani.kotlin.findbus.beans.Arrives
import com.dani.kotlin.findbus.beans.Stop
import com.dani.kotlin.findbus.beans.Stops
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class FindBusConnector {
    val url = "https://servicios.emtmadrid.es:8443/geo/servicegeo.asmx/"
    val usuario = "WEB.SERV.dani.morato.20@gmail.com"
    val pass = "05AB8EEB-D902-4EEA-9010-C342D4B60754"

    fun getStopsFromXY(x: Double, y: Double) : Stops {
        val uri = url + "getStopsFromXY?idClient=" + usuario +
            "&passKey=" + pass + "&coordinateX=" + x + "&coordinateY=" +
            y + "&Radius=400&statistics=&cultureInfo=ES"

        val json = getJSON(uri)
        val gson = Gson()
        val stops: Stops = gson.fromJson(
            json, Stops::class.java)

        return stops
    }

    fun getArriveStop(idStop: String) : Arrives {
        val uri = url + "getArriveStop?idClient=" +
            usuario + "&passKey=" + pass + "&idStop=" +
            idStop + "&statistics=&cultureInfo=ES"

        val json = getJSON(uri)
        val gson = Gson()
        val arrives: Arrives = gson.fromJson(
            json, Arrives::class.java)

        return arrives
    }

    private fun getJSON(uri: String) : String {
        val connection = URL(uri).openConnection()
            as HttpURLConnection

        connection.connect()
        println(connection.responseCode)
        println(connection.getHeaderField(
            "Content-Type"))

        val result = connection.inputStream.use {
            it.reader().use { reader -> reader.readText() }
        }

        return result
    }
}