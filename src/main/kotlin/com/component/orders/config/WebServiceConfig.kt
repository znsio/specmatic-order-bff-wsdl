package com.component.orders.config

import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.config.annotation.EnableWs
import org.springframework.ws.config.annotation.WsConfigurerAdapter
import org.springframework.ws.server.EndpointInterceptor
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor
import org.springframework.ws.transport.http.MessageDispatcherServlet


@EnableWs
@Configuration
class WebServiceConfig : WsConfigurerAdapter() {
    @Bean
    fun messageDispatcherServlet(applicationContext: ApplicationContext): ServletRegistrationBean<MessageDispatcherServlet> {
        val servlet = MessageDispatcherServlet()
        servlet.setApplicationContext(applicationContext)
        servlet.isTransformWsdlLocations = true
        return ServletRegistrationBean(servlet, "/ws/*")
    }

    @Bean
    fun marshaller(): Jaxb2Marshaller {
        val marshaller = Jaxb2Marshaller()
        marshaller.setContextPath("com.example.orders")
        return marshaller
    }

    override fun addInterceptors(interceptors: MutableList<EndpointInterceptor>) {
        // Add logging to help debug SOAP requests
        val loggingInterceptor = PayloadLoggingInterceptor()
        loggingInterceptor.setLogRequest(true)
        loggingInterceptor.setLogResponse(true)
        interceptors.add(loggingInterceptor)
    }
}
