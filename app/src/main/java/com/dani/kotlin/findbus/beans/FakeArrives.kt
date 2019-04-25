package com.dani.kotlin.findbus.beans

import org.ksoap2.serialization.SoapObject

class FakeArrives {

    val arrives: Arrives

    init {
        val soap = SoapObject()
        val data = listOf<Any>(
            listOf("1773", "M2", "True", "ARGÜELLES", "9066", "861", "1188", "-3,70205392091662", "40,4236668600365", "1"),
            listOf("1773", "M2", "True", "ARGÜELLES", "9061", "999999", "3696", "-3,71412140046972", "40,4299565569191", "1")
        )

        val soapArrives = buildArriveObjects(data)
        soap.addProperty("CoordinateX", -3.622523)
        soap.addProperty("CoordinateY", 40.434547)
        soap.addProperty("Arrive", soapArrives[0])
        soap.addProperty("Arrive", soapArrives[1])

        arrives = Arrives(soap)
    }

    private fun buildArriveObjects(data: List<Any>): List<SoapObject> {
        val arrives = mutableListOf<SoapObject>()

        for (element in data) {
            val soap = SoapObject()
            val arrive = element as List<String>

            soap.addProperty("ResponseCode", "200")
            soap.addProperty("Message", "FakeStop")
            soap.addProperty("IdStop", arrive[0])
            soap.addProperty("idLine", arrive[1])
            soap.addProperty("IsHead", arrive[2])
            soap.addProperty("Destination", arrive[3])
            soap.addProperty("IdBus",  arrive[4])
            soap.addProperty("TimeLeftBus",  arrive[5])
            soap.addProperty("DistanceBus",  arrive[6])
            soap.addProperty("PositionXBus", arrive[7])
            soap.addProperty("PositionYBus", arrive[8])
            soap.addProperty("PositionTypeBus", arrive[9])

            arrives.add(soap)
        }

        return arrives
    }
}