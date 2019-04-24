package com.dani.kotlin.findbus.util

import org.ksoap2.serialization.SoapSerializationEnvelope
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParser
import org.ksoap2.serialization.Marshal
import org.ksoap2.serialization.PropertyInfo
import org.xmlpull.v1.XmlSerializer
import java.io.IOException

class MarshalDouble : Marshal {

    @Throws(IOException::class, XmlPullParserException::class)
    override fun readInstance(
        parser: XmlPullParser, namespace: String,
        name: String, expected: PropertyInfo): Any {

        //return java.lang.Double.parseDouble(parser.nextText())
        return parser.nextText() to Double
    }

    override fun register(cm: SoapSerializationEnvelope) {
        cm.addMapping(cm.xsd, "double", Double::class.java, this)
    }

    @Throws(IOException::class)
    override fun writeInstance(writer: XmlSerializer, obj: Any) {
        writer.text(obj.toString())
    }
}