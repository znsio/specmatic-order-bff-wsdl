package com.component.orders.controllers

import com.component.orders.models.OrderRequest
import com.component.orders.models.OrderResponse
import com.component.orders.models.SoapResponse
import com.component.orders.services.OrderBFFService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Orders(@Autowired val orderBFFService: OrderBFFService) {
    @PostMapping("/orders", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createOrder(@Valid @RequestBody orderRequest: OrderRequest): ResponseEntity<String> {
        val orderResponse = orderBFFService.createOrder(orderRequest)
        val soapResponse = SoapResponse()
        val soapMessage = soapResponse.createSoapResponse(orderResponse)
        val soapMessageString = soapResponse.mapSoapResponseToString(soapMessage)
        return ResponseEntity(soapMessageString, HttpHeaders().apply { add("Content-Type", "application/soap+xml") }, HttpStatus.CREATED)
    }
}