package com.dani.kotlin.findbus.beans

import org.ksoap2.serialization.SoapObject

class Line(stop: SoapObject) {

    private var idLine: String = ""
    private var label: String = ""
    private var headerA: String = ""
    private var headerB: String = ""
    private var direction: String = ""
    private var dayType: String = ""
    private var startTime: String = ""
    private var stopTime: String = ""
    private var minimumFrecuency: String = ""
    private var maximumFrecuency: String = ""
    private var tipoDia: String = ""
    private var frecuencyS1: String = ""
    private var frecuencyS2: String = ""

    init {
        val line: SoapObject = stop.getProperty("Line") as SoapObject
        idLine = line.getPropertyAsString("IdLine")
        label = line.getPropertyAsString("Label")
        headerA = line.getPropertyAsString("HeaderA")
        headerB = line.getPropertyAsString("HeaderB")
        direction = line.getPropertyAsString("Direction")
        dayType = line.getPropertyAsString("DayType")
        startTime = line.getPropertyAsString("StartTime")
        stopTime = line.getPropertyAsString("StopTime")
        minimumFrecuency = line.getPropertyAsString("MinimumFrecuency")
        maximumFrecuency = line.getPropertyAsString("MaximumFrecuency")
        tipoDia = line.getPropertyAsString("TipoDia")
        frecuencyS1 = line.getPropertyAsString("FrecuencyS1")
        frecuencyS2 = line.getPropertyAsString("FrecuencyS2")
    }
}