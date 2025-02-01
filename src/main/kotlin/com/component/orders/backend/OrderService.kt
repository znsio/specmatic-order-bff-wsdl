package com.component.orders.backend

import com.component.orders.models.*
import com.component.orders.models.messages.FindAvailableProductsRequest
import com.component.orders.models.messages.FindAvailableProductsResponse
import com.component.orders.models.messages.ProductListWrapper
import com.component.orders.models.messages.ProductMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.ws.client.core.WebServiceTemplate
import java.lang.IllegalStateException
import java.util.*

@Service
class OrderService(private val jacksonObjectMapper: ObjectMapper) {
    private val authToken = "API-TOKEN-SPEC"

    @Value("\${order.api}")
    lateinit var orderAPIUrl: String

    @Value("\${kafka.bootstrap-servers}")
    lateinit var kafkaBootstrapServers: String

    @Value("\${kafka.topic}")
    lateinit var kafkaTopic: String

    fun createOrder(orderRequest: OrderRequest): Int {
        val apiUrl = orderAPIUrl + "/" + API.CREATE_ORDER.url

        val marshaller = Jaxb2Marshaller().apply {
            setClassesToBeBound(Order::class.java, Id::class.java)
        }

        val webServiceTemplate = WebServiceTemplate(marshaller).apply {
            setMarshaller(marshaller)
            unmarshaller = marshaller
        }

        val request = Order(orderRequest.productid, orderRequest.count, "pending")
        val response = webServiceTemplate.marshalSendAndReceive(apiUrl, request) as Id

        return response.id
    }

    fun findProducts(type: String): List<Product> {
        val products = fetchFirstProductFromSOAPAPI(type)
        return products
    }

    fun createProduct(newProduct: NewProduct): Int {
        val apiUrl = orderAPIUrl + "/" + API.CREATE_PRODUCTS.url

        val marshaller = Jaxb2Marshaller().apply {
            setClassesToBeBound(NewProduct::class.java, Id::class.java)
        }

        val webServiceTemplate = WebServiceTemplate(marshaller).apply {
            setMarshaller(marshaller)
            unmarshaller = marshaller
        }

        val request = NewProduct(newProduct.name, newProduct.type, newProduct.inventory)
        val response = webServiceTemplate.marshalSendAndReceive(apiUrl, request) as Id

        return response.id
    }

    private fun fetchFirstProductFromSOAPAPI(type: String): List<Product> {
        val webServiceTemplate = WebServiceTemplate()
        val apiUrl = orderAPIUrl + "/" + API.LIST_PRODUCTS.url
        val marshaller = Jaxb2Marshaller()
        marshaller.setClassesToBeBound(
            FindAvailableProductsRequest::class.java,
            FindAvailableProductsResponse::class.java,
            Product::class.java,
            ProductListWrapper::class.java
        )

        webServiceTemplate.marshaller = marshaller
        webServiceTemplate.unmarshaller = marshaller

        val request = FindAvailableProductsRequest(type = type)
        val response = webServiceTemplate.marshalSendAndReceive(apiUrl, request) as FindAvailableProductsResponse

        val products = response.arrayList?.products ?: emptyList()

        if (products.any { it.type != type }) {
            throw IllegalStateException("Product type mismatch")
        }

        return products.take(1)
    }

    private fun getKafkaProducer(): KafkaProducer<String, String> {
        val props = Properties()
        println("kafkaBootstrapServers: $kafkaBootstrapServers")
        props["bootstrap.servers"] = kafkaBootstrapServers
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        return KafkaProducer<String, String>(props)
    }
}