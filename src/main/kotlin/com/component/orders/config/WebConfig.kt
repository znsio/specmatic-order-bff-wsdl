//package com.component.orders.config
//
//import com.component.orders.models.NewProduct
//import com.component.orders.models.SoapRequestDeserializer
//import com.fasterxml.jackson.databind.module.SimpleModule
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
//
//@Configuration
//class WebConfig {
//
//    @Bean
//    fun objectMapper(): ObjectMapper {
//        val objectMapper = ObjectMapper()
//        val module = SimpleModule()
//
//        module.addDeserializer(NewProduct::class.java, SoapRequestDeserializer(NewProduct::class.java))
//        objectMapper.registerModule(module)
//
//        return objectMapper
//    }
//
//    @Bean
//    fun jackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
//        return MappingJackson2HttpMessageConverter(objectMapper())
//    }
//}
