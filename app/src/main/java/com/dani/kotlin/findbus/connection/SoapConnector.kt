package com.dani.kotlin.findbus.connection

import android.content.Context
import com.dani.kotlin.findbus.R
import com.dani.kotlin.findbus.models.Arrives
import com.dani.kotlin.findbus.models.FakeArrives
import com.dani.kotlin.findbus.models.FakeStops
import com.dani.kotlin.findbus.models.Stops
import org.jetbrains.anko.doAsync
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.transport.HttpTransportSE
import org.ksoap2.serialization.MarshalFloat
import org.w3c.dom.Element
import org.w3c.dom.Node

class SoapConnector(context: Context) {
    private lateinit var operationName: String
    private val username: String
    private val password: String
    private val ENDPOINT: String

    init {
        username = context.getString(R.string.web_api_username)
        password = context.getString(R.string.web_api_password)
        ENDPOINT = context.getString(R.string.web_api_endpoint)
    }

    private fun buildEnvelopObject(): SoapSerializationEnvelope {
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER12)
        envelope.dotNet = true
        envelope.implicitTypes = true
        envelope.isAddAdornments = false
        envelope.encodingStyle = SoapSerializationEnvelope.XSD

        val marshal = MarshalFloat()
        marshal.register(envelope)

        return envelope
    }

    private fun buildBodyObject(): SoapObject {
        val body = SoapObject(ENDPOINT, operationName)

        // HINT: Use a map (Map<ParamName, ParamValue>) when you have several parameters
        body.addProperty("idClient", username)
        body.addProperty("passKey",  password)
        body.addProperty("statistics",  "")
        body.addProperty("cultureInfo", "ES")

        return body
    }

    fun getStopsFromXY(x: Double, y: Double, radius: Double): Stops {
        operationName = "getStopsFromXY"
        val envelope = buildEnvelopObject()
        val body = buildBodyObject()
//        envelope.headerOut = Element().createElement(
//            "SoapAction", operationName)

        body.addProperty("coordinateX", x)
        body.addProperty("coordinateY", y)
        body.addProperty("Radius", radius)
        envelope.setOutputSoapObject(body)

        // val stops = FakeStops().stops
        // return stops
        return Stops(getSoapXML(envelope))
    }

    fun getArriveStop(idStop: String): Arrives {
        operationName = "getArriveStop"
        val envelope = buildEnvelopObject()
        val body = buildBodyObject()
//        envelope.headerOut = Element().createElement(
//            "SoapAction", operationName)

        body.addProperty("idStop", idStop)
        envelope.setOutputSoapObject(body)

        // val arrives = FakeArrives().arrives
        // return arrives
        return Arrives(getSoapXML(envelope))
    }

    private fun getSoapXML(envelope: SoapSerializationEnvelope): SoapObject {
        val httpTransportSE = HttpTransportSE(ENDPOINT)
        val soapAction = ENDPOINT + operationName
        httpTransportSE.debug = true

        doAsync { httpTransportSE.call(soapAction, envelope) }

        Thread.sleep(3000)
        return envelope.response as SoapObject
    }
}