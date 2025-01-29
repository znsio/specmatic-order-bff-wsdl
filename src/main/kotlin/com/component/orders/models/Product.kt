package com.component.orders.models

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import java.util.concurrent.atomic.AtomicInteger
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@XmlRootElement
data class Product(
    @field:NotNull
    @field:XmlElement(required = true)
    val name: String = "",

    @field:NotNull
    @field:XmlElement(required = true)
    val type: String = "gadget",

    @field:Positive
    @field:XmlElement(required = true)
    val inventory: Int = 0,

    @field:XmlElement(required = true)
    val id: Int = idGenerator.getAndIncrement()
) {
    companion object {
        val idGenerator: AtomicInteger = AtomicInteger()
    }
}