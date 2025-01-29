package com.component.orders.models

import jakarta.xml.soap.*

 class SoapResponse {
     fun createSoapResponse(responseData: Any): SOAPMessage {
        val messageFactory = MessageFactory.newInstance()
        val soapMessage = messageFactory.createMessage()
        val soapEnvelope: SOAPEnvelope = soapMessage.soapPart.envelope
        val soapBody: SOAPBody = soapEnvelope.body

        val responseElement: SOAPElement = soapBody.addChildElement(responseData.javaClass.simpleName)
        when (responseData) {
            is OrderResponse -> {
                responseElement.addChildElement("id").value = responseData.id.toString()
            }

            is List<*> -> {
                val productsElement = responseElement.addChildElement("products")
                (responseData as List<Product>).forEach { product ->
                    val productElement = productsElement.addChildElement("product")
                    productElement.addChildElement("id").value = product.id.toString()
                    productElement.addChildElement("name").value = product.name
                    productElement.addChildElement("type").value = product.type
                    productElement.addChildElement("inventory").value = product.inventory.toString()
                }
            }

            is Id -> {
                responseElement.addChildElement("id").value = responseData.id.toString()
            }
        }
        return soapMessage
    }

     fun mapSoapResponseToString(soapMessage: SOAPMessage): String {
        val byteArrayOutputStream = java.io.ByteArrayOutputStream()
        soapMessage.writeTo(byteArrayOutputStream)
        return byteArrayOutputStream.toString("UTF-8")
    }
}
