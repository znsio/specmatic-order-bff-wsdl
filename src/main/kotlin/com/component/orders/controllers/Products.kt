package com.component.orders.controllers

import com.component.orders.models.Id
import com.component.orders.models.NewProduct
import com.component.orders.models.Product
import com.component.orders.models.SoapResponse
import com.component.orders.services.OrderBFFService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders

@RestController
@Validated
class Products(@Autowired val orderBFFService: OrderBFFService) {
    val soapResponse = SoapResponse()
    @GetMapping("/findAvailableProducts", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAvailableProducts(
        @RequestParam(
            name = "type",
            required = false,
            defaultValue = "gadget"
        ) type: String,
        @RequestHeader(
            name = "pageSize",
            required = true
        ) pageSize: Int
    ): ResponseEntity<String> {
        if (pageSize < 0) throw IllegalArgumentException("pageSize must be positive")
        val productsResponse = orderBFFService.findProducts(type)
        val soapMessage = soapResponse.createSoapResponse(productsResponse)
        val soapMessageString = soapResponse.mapSoapResponseToString(soapMessage)
        return ResponseEntity(soapMessageString, HttpHeaders().apply { add("Content-Type", "application/soap+xml") }, HttpStatus.OK)
    }

    @PostMapping("/products", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createProduct(@RequestBody newProduct: NewProduct): ResponseEntity<String> {
        val productResponse = orderBFFService.createProduct(newProduct)
        val soapMessage = soapResponse.createSoapResponse(productResponse)
        val soapMessageString = soapResponse.mapSoapResponseToString(soapMessage)
        return ResponseEntity(soapMessageString, HttpHeaders().apply { add("Content-Type", "application/soap+xml") }, HttpStatus.CREATED)
    }
}