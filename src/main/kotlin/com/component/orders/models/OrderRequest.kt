package com.component.orders.models

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class OrderRequest(
    @field:XmlElement(required = true)
    val productid: Int? = null,

    @field:XmlElement(required = true)
    val count: Int? = null
)