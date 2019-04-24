package com.dani.kotlin.findbus.connection

import com.dani.kotlin.findbus.beans.Arrives
import com.dani.kotlin.findbus.beans.Stops
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.transport.HttpTransportSE
import com.dani.kotlin.findbus.util.MarshalDouble
import kotlinx.coroutines.*
import org.ksoap2.serialization.MarshalFloat
import kotlin.concurrent.thread

class SoapConnector {
    private lateinit var OPERATION_NAME: String
    private val user = "WEB.SERV.dani_morato_19@hotmail.com"
    private val pass = "655362EA-55F9-4236-AC14-729FBC7B659C"
    private var TARGET_NAMESPACE =
        "https://servicios.emtmadrid.es:8443/geo/servicegeo.asmx/"

    fun buildEnvelopObject(): SoapSerializationEnvelope {
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER12)
        envelope.dotNet = true
        envelope.implicitTypes = true
        envelope.isAddAdornments = false
        envelope.encodingStyle = SoapSerializationEnvelope.XSD
        val marshal = MarshalFloat()
        marshal.register(envelope)

        return envelope
    }

    fun buildBodyObject(): SoapObject {
        val body = SoapObject(TARGET_NAMESPACE, OPERATION_NAME)

        // HINT: Use a map (Map<ParamName, ParamValue>) when you have several parameters
        body.addProperty("idClient", user)
        body.addProperty("passKey",  pass)
        body.addProperty("statistics",  "")
        body.addProperty("cultureInfo", "ES")

        return body
    }

    fun getStopsFromXY(x: Double, y: Double, radius: Double): Stops {
        OPERATION_NAME = "getStopsFromXY"
        val envelope = buildEnvelopObject()
        val body = buildBodyObject()
        val stops: Stops

        body.addProperty("coordinateX", x)
        body.addProperty("coordinateY", y)
        body.addProperty("Radius", radius)
        envelope.setOutputSoapObject(body)

        stops = Stops(getSoapXML(envelope))
        return stops
    }

    fun getArriveStop(idStop: String): Arrives {
        OPERATION_NAME = "getArriveStop"
        val envelope = buildEnvelopObject()
        val body = buildBodyObject()
        val arrives: Arrives

        body.addProperty("idStop", idStop)
        envelope.setOutputSoapObject(body)

        arrives = Arrives(getSoapXML(envelope))
        return arrives
    }

    fun getSoapXML(envelope: SoapSerializationEnvelope): SoapObject {
        val response: SoapObject
        val httpTransportSE = HttpTransportSE(TARGET_NAMESPACE)
        httpTransportSE.debug = true

        val soapAction = TARGET_NAMESPACE + OPERATION_NAME
        GlobalScope.launch(Dispatchers.Main) {
            httpTransportSE.call(soapAction, envelope)
        }

        println("SOAP Request "  + httpTransportSE.requestDump)
        println("SOAP Response " + httpTransportSE.responseDump)

        response = when {
            (envelope.response != null) ->
                envelope.response as SoapObject
            else -> SoapObject()
        }

        return response
    }
}