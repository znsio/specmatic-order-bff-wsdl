package com.component.orders.models

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import jakarta.xml.bind.Unmarshaller
import jakarta.xml.soap.MessageFactory
import jakarta.xml.soap.Node
import jakarta.xml.soap.SOAPMessage
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

class SoapRequestDeserializer<T>(private val targetClass: Class<T>) : JsonDeserializer<T>() {
    private val objectMapper = ObjectMapper()
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T? {
        val xmlContent = p.readValueAsTree<JsonNode>()
        val soapSubject = xmlContent.elements().next().elements().asSequence().first()

        return objectMapper.treeToValue(soapSubject, targetClass)
    }
}
