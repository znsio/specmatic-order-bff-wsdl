package com.component.orders.models
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class NewProduct(
    @field:NotNull
    @field:XmlElement(required = true)
    val name: String = "",

    @field:NotNull
    @field:XmlElement(required = true)
    val type: String = "gadget",

    @field:NotNull
    @field:Min(1)
    @field:Max(101)
    @field:XmlElement(required = true)
    val inventory: Int? = 1
)
