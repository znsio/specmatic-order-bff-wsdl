package com.component.orders.config

import com.component.orders.models.NewProduct
import com.component.orders.models.OrderRequest
import com.component.orders.models.SoapRequestDeserializer
import com.component.orders.models.StrictStringDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class StringStringDeserializerConfig {
    @Bean
    open fun strictStringDeserializerModule(): SimpleModule {
        val module = SimpleModule()
        module.addDeserializer(String::class.java, StrictStringDeserializer())
        module.addDeserializer(NewProduct::class.java, SoapRequestDeserializer(NewProduct::class.java))
        module.addDeserializer(OrderRequest::class.java, SoapRequestDeserializer(OrderRequest::class.java))
        return module
    }
}