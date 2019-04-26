package com.dani.kotlin.findbus.models

import org.ksoap2.serialization.SoapObject

class Stops(response: SoapObject) {
    val elements: List<Stop>

    init {
        elements = mutableListOf()

        for (currentStop in 2 until response.propertyCount) {
            val stopSoapObject = response.getProperty(currentStop) as SoapObject
            val stop = Stop(stopSoapObject)
            elements.add(stop)
        }
    }
}