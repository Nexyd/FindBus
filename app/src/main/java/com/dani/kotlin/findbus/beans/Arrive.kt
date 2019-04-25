package com.dani.kotlin.findbus.beans

import org.ksoap2.serialization.SoapObject

class Arrive(response: SoapObject) {

    var responseCode: String = ""
    var responseMessage: String = ""
    var idStop: String = ""
    var idLine: String = ""
    var isHead: String = ""
    var destination: String = ""
    var idBus: String = ""
    var timeLeftBus: String = ""
    var distanceBus: String = ""
    var positionXBus: String = ""
    var positionYBus: String = ""
    var positionTypeBus: String = ""

    init {
        responseCode = response.getPrimitivePropertyAsString("ResponseCode")
        responseMessage = response.getPrimitivePropertyAsString("Message")

        idStop = response.getPropertyAsString("IdStop")
        idLine = response.getPropertyAsString("idLine")
        isHead = response.getPropertyAsString("IsHead")
        destination = response.getPropertyAsString("Destination")
        idBus = response.getPropertyAsString("IdBus")
        timeLeftBus = response.getPropertyAsString("TimeLeftBus")
        distanceBus = response.getPropertyAsString("DistanceBus")
        positionXBus = response.getPropertyAsString("PositionXBus")
        positionYBus = response.getPropertyAsString("PositionYBus")
        positionTypeBus = response.getPropertyAsString("PositionTypeBus")
    }

    fun getTimeArrival(): String {
        val time = timeLeftBus.toInt() / 60
        var result: String

        if (time > 30) result = " más de 20 min."
        if (time == 0) result = " está en la parada."
        else result = time.toString() + " min."

        return result
    }
}