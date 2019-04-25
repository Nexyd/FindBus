package com.dani.kotlin.findbus.beans

import org.ksoap2.serialization.SoapObject

class FakeStops {

    val stops: Stops

    init {
        val soap = SoapObject()
        val data = listOf<Any>(
            listOf("3762", "", "Emilio Muñoz-Miguel Yuste", "Miguel Yuste con C/ Emilio Muñoz", "-3.62258637739425", "40.4344679422618"),
            listOf("985",  "", "Emilio Muñoz-Miguel Yuste", "Emilio Muñoz, 57", "-3.62284691464877", "40.4345926680148"),
            listOf("3015", "", "Emilio Muñoz-Miguel Yuste", "Miguel Yuste con C/ Cronos", "-3.62225576541349", "40.4344156647035"),
            listOf("984",  "", "Emilio Muñoz-Miguel Yuste", "Emilio Muñoz frente al Nº 57", "-3.62307930862", "40.434222047573")
        )

        val soapStops = buildStopObjects(data)

        soap.addProperty("CoordinateX", -3.622523)
        soap.addProperty("CoordinateY", 40.434547)
        soap.addProperty("Stop", soapStops[0])
        soap.addProperty("Stop", soapStops[1])
        soap.addProperty("Stop", soapStops[2])
        soap.addProperty("Stop", soapStops[3])

        stops = Stops(soap)
    }

    private fun buildStopObjects(data: List<Any>): List<SoapObject> {
        val stops = mutableListOf<SoapObject>()

        for (element in data) {
            val soap = SoapObject()
            val stop = element as List<String>

            soap.addProperty("ResponseCode", "200")
            soap.addProperty("Message", "FakeStop")
            soap.addProperty("IdStop", stop[0])
            soap.addProperty("PMV",  stop[1])
            soap.addProperty("Name", stop[2])
            soap.addProperty("PostalAdress", stop[3])
            soap.addProperty("CoordinateX",  stop[4])
            soap.addProperty("CoordinateY",  stop[5])
            //soap.addProperty("Line", Line())

            stops.add(soap)
        }

        return stops
    }
}