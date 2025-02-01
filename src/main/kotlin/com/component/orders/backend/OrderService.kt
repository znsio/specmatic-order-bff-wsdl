package com.component.orders.backend

import com.component.orders.models.*
import com.component.orders.models.messages.FindAvailableProductsRequest
import com.component.orders.models.messages.FindAvailableProductsResponse
import com.component.orders.models.messages.ProductListWrapper
import com.example.orders.OrderAPIService
import org.springframework.beans.factory.annotation.Value
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate
import java.io.File

@Service
class OrderService {
    @Value("\${order.api}")
    lateinit var orderAPIUrl: String

    var orderService: OrderAPIService =
        OrderAPIService(
            File("wsdls/order_api.wsdl").canonicalFile.toURI().toURL())

    fun createOrder(orderRequest: OrderRequest): Int {
        val downstreamOrderRequest = com.example.orders.CreateOrder()
        downstreamOrderRequest.productid = orderRequest.count ?: 1
        downstreamOrderRequest.productid = orderRequest.productid ?: throw Exception("Product id not supplied in order request")

        val orderServicePort = orderService.getOrderAPIPort()

        try {
            val orderResponse = orderServicePort.createOrder(downstreamOrderRequest)
            return orderResponse.id
        } catch(e: Throwable) {
            println(e.message ?: e.localizedMessage)
            e.printStackTrace()
            throw e
        }
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
}