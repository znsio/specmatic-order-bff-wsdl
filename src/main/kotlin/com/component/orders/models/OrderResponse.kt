package com.component.orders.models

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class OrderResponse(
    @field:XmlElement(required = true)
    val id: Int
)