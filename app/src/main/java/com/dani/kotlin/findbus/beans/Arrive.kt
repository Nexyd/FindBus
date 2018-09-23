package com.dani.kotlin.findbus.beans
import com.google.gson.annotations.SerializedName

class Arrive {

    @SerializedName("idStop")
    var idStop: String = ""
    @SerializedName("idLine")
    var idLine: String = ""
    @SerializedName("isHead")
    var isHead: String = ""
    @SerializedName("idBus")
    var idBus: String = ""
    @SerializedName("timeLeftBus")
    var timeLeftBus: String = ""
    @SerializedName("distanceBus")
    var distanceBus: String = ""
    @SerializedName("positionXBus")
    var positionXBus: String = ""
    @SerializedName("positionYBus")
    var positionYBus: String = ""
    @SerializedName("destination")
    var destination: String = ""
    @SerializedName("positionTypeBus")
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

abstract class Arrives : List<Arrive>