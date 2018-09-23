package com.dani.kotlin.findbus.beans

class Arrive {
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

    fun timeInMin(): String {
        val time = timeLeftBus.toInt() / 60
        var result: String

        if (time > 30) result = " más de 20 min."
        if (time == 0) result = " está en la parada."
        else result = time.toString() + " min."

        return result
    }
}