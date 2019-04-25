package com.dani.kotlin.findbus.beans

import org.ksoap2.serialization.SoapObject

class Stop(response: SoapObject) {

    var responseCode: String = ""
    var responseMessage: String = ""
    var idStop: String = ""
    var pmv: String = ""
    var name: String = ""
    var postalAdress: String = ""
    var coordinateX: String = ""
    var coordinateY: String = ""
    //var line: Line

    init {
        responseCode = response.getPrimitivePropertyAsString("ResponseCode")
        responseMessage = response.getPrimitivePropertyAsString("Message")

        idStop = response.getPropertyAsString("IdStop")
        pmv = response.getPropertyAsString("PMV")
        name = response.getPropertyAsString("Name")
        postalAdress = response.getPropertyAsString("PostalAdress")
        coordinateX = response.getPropertyAsString("CoordinateX")
        coordinateY = response.getPropertyAsString("CoordinateY")

        //line = Line(response)
    }
}