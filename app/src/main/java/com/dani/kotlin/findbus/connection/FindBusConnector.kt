package com.dani.kotlin.findbus.connection

import java.net.HttpURLConnection
import java.net.URL

class FindBusConnector {
    val url = "https://servicios.emtmadrid.es:8443/geo/servicegeo.asmx/"
    val usuario = "WEB.SERV.dani.morato.20@gmail.com"
    val pass = "05AB8EEB-D902-4EEA-9010-C342D4B60754"

    fun getStopsFromXY(x: Double, y: Double) : String {
        val uri = url + "getStopsFromXY?idClient=" + usuario +
            "&passKey=" + pass + "&coordinateX=" + x + "&coordinateY=" +
            y + "&Radius=400&statistics=&cultureInfo=ES"

        return getJSON(uri)
    }

    fun getArriveStop(idStop: String) : String {
        val uri = url + "getArriveStop?idClient=" +
            usuario + "&passKey=" + pass + "&idStop=" +
            idStop + "&statistics=&cultureInfo=ES"

        return getJSON(uri)
    }

    private fun getJSON(uri: String) : String {
        val connection = URL(uri).openConnection()
            as HttpURLConnection

        connection.connect()
        println(connection.responseCode)
        println(connection.getHeaderField("Content-Type"))

        val result = connection.inputStream.use {
            it.reader().use { reader -> reader.readText() }
        }

        return result
    }
}

// import kotlinx.coroutines.experimental.*
// fun getJSONAsync(uri: String) {
//     async {
//         val result = URL("<api call>").readText()
//         uiThread {
//             Log.d("Request", result)
//             longToast("Request performed")
//         }
//     }
// }