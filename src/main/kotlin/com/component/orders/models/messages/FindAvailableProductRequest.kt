package com.component.orders.models.messages

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "findAvailableProductsRequest")
data class FindAvailableProductsRequest(
    @XmlElement(name = "type")
    val type: String = ""
)