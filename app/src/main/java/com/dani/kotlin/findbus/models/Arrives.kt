package com.dani.kotlin.findbus.models

import org.ksoap2.serialization.SoapObject

class Arrives(response: SoapObject) {
    val elements: List<Arrive>

    init {
        elements = mutableListOf()
        for (i in 3..response.propertyCount) {
            val arriveSoapObject = response.getProperty("Arrive") as SoapObject
            val arrive = Arrive(arriveSoapObject)
            elements.add(arrive)
        }
    }
}