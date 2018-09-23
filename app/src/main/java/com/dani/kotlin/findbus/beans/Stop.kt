package com.dani.kotlin.findbus.beans
import com.google.gson.annotations.SerializedName

class Stop {

    @SerializedName("idStop")
    var idStop: String = ""
    @SerializedName("pmv")
    var pmv: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("postalAdress")
    var postalAdress: String = ""
    @SerializedName("coordinateX")
    var coordinateX: String = ""
    @SerializedName("coordinateY")
    var coordinateY: String = ""
}

abstract class Stops : List<Stop>