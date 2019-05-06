package com.dani.kotlin.findbus.connection

import android.content.Context
import com.dani.kotlin.findbus.R
import com.dani.kotlin.findbus.models.Arrives
import com.dani.kotlin.findbus.models.Stops
import org.jetbrains.anko.doAsync
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.transport.HttpTransportSE
import org.ksoap2.serialization.MarshalFloat

class SoapConnector(context: Context) {
    private lateinit var operationName: String
    private val username:  String
    private val password:  String
    private val ENDPOINT:  String
    private val NAMESPACE: String

    init {
        username  = context.getString(R.string.web_api_username)
        password  = context.getString(R.string.web_api_password)
        ENDPOINT  = context.getString(R.string.web_api_endpoint)
        NAMESPACE = context.getString(R.string.web_api_soap_action)
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
        val body = SoapObject(NAMESPACE, operationName)

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

        body.addProperty("coordinateX", x)
        body.addProperty("coordinateY", y)
        body.addProperty("Radius", radius)
        envelope.setOutputSoapObject(body)

        return Stops(getSoapXML(envelope))
    }

    fun getArriveStop(idStop: String): Arrives {
        operationName = "getArriveStop"
        val envelope = buildEnvelopObject()
        val body = buildBodyObject()

        body.addProperty("idStop", idStop)
        envelope.setOutputSoapObject(body)

        return Arrives(getSoapXML(envelope))
    }

    private fun getSoapXML(envelope: SoapSerializationEnvelope): SoapObject {
        val httpTransportSE = HttpTransportSE(ENDPOINT)
        val soapAction = "$NAMESPACE$operationName"
        httpTransportSE.debug = true

        // TODO: Investigate why using coordinates outside the community of madrid it yields an empty array (no stops around)
        // TODO: But if coordinates inside the community of madrid are used it yields null (when it should be backwards)
        doAsync { httpTransportSE.call(soapAction, envelope) }

        //return envelope.bodyIn as SoapObject
        return envelope.response as SoapObject
    }
}