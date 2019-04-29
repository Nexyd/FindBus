package com.dani.kotlin.findbus.connection

import android.content.Context
import com.dani.kotlin.findbus.MapsActivity
import com.dani.kotlin.findbus.R
import com.dani.kotlin.findbus.models.Arrives
import com.dani.kotlin.findbus.models.FakeArrives
import com.dani.kotlin.findbus.models.FakeStops
import com.dani.kotlin.findbus.models.Stops
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.transport.HttpTransportSE
import org.ksoap2.serialization.MarshalFloat

class SoapConnector(val context: Context) {
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
        val response: Observable<Any>
        val envelope = buildEnvelopObject()
        val body = buildBodyObject()
        val stops: Stops

        body.addProperty("coordinateX", x)
        body.addProperty("coordinateY", y)
        body.addProperty("Radius", radius)
        envelope.setOutputSoapObject(body)

        //response = getSoapXML(envelope)
        //stops = Stops(response.blockingFirst(
        //    SoapObject()) as SoapObject)

        stops = FakeStops().stops
        return stops
    }

    fun getArriveStop(idStop: String): Arrives {
        operationName = "getArriveStop"
        val response: Observable<Any>
        val envelope = buildEnvelopObject()
        val body = buildBodyObject()
        val arrives: Arrives

        body.addProperty("idStop", idStop)
        envelope.setOutputSoapObject(body)

        // response = getSoapXML(envelope)
        // arrives = Arrives(response.blockingFirst(
        //     SoapObject()) as SoapObject)

        arrives = FakeArrives().arrives
        return arrives
    }

    private fun getSoapXML(envelope: SoapSerializationEnvelope): Observable<Any> {
        val httpTransportSE = HttpTransportSE(ENDPOINT)
        httpTransportSE.debug = true

        val soapAction = ENDPOINT + operationName
        val response: Observable<Any> =
            Observable.just(envelope.response)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

        response.subscribeBy(
            onNext  = { httpTransportSE.call(soapAction, envelope) },
            onError = { it.printStackTrace() },
            onComplete = { println(envelope.response) }
        ).addTo(MapsActivity.COMPOSITE_DISPOSABLE)

        println("SOAP Request "  + httpTransportSE.requestDump)
        println("SOAP Response " + httpTransportSE.responseDump)

        return response
    }
}