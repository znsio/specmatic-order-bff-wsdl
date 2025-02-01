package com.component.orders.models.messages

import com.component.orders.models.Product
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "findAvailableProductsResponse")
data class FindAvailableProductsResponse(
    @XmlElement(name = "arrayList")
    val arrayList: ProductListWrapper? = null
)

@XmlRootElement(name = "arrayList")
data class ProductListWrapper(
    @XmlElement(name = "products")
    val products: List<Product>? = null
)