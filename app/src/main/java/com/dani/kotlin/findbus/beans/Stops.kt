package com.dani.kotlin.findbus.beans

import org.ksoap2.serialization.SoapObject

class Stops(response: SoapObject) {
    val elements: List<Stop>

    init {
        elements = mutableListOf()
        for (i in 3..response.propertyCount) {
            val stopSoapObject = response.getProperty("Stop") as SoapObject
            val stop = Stop(stopSoapObject)
            elements.add(stop)
        }
    }
}