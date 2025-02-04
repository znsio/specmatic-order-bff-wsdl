package com.component.orders.controllers

import com.example.orders.OrderRequest
import com.example.orders.OrderResponse
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload


@Endpoint
class OrderEndpoint {

    companion object {
        const val NAMESPACE_URI: String = "http://www.example.com/orders"
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderRequest")
    @ResponsePayload
    fun createOrder(@RequestPayload request: OrderRequest): OrderResponse {
        val response = OrderResponse()
        response.id = 10 // Example response, replace with actual logic
        return response
    }
}