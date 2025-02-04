package com.component.orders.apiTest

import com.example.orders.OrderRequest
import com.example.orders.OrderService
import io.specmatic.stub.ContractStub
import io.specmatic.stub.createStub
import jakarta.xml.ws.BindingProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class APITest {
    companion object {
        private const val SOAP_STUB_HOST = "localhost"
        private const val SOAP_STUB_PORT = 8090

        private var soapStub: ContractStub? = null

        @BeforeAll
        @JvmStatic
        fun setup() {
            soapStub = createStub(
                host = SOAP_STUB_HOST,
                port = SOAP_STUB_PORT,
                strict = true)
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            soapStub?.close()
        }
    }

    @Test
    fun `create an order`() {
        val orderRequest = OrderRequest().apply {
            productid = 123
            count = 1
        }

        val wsdlFileURL = File("wsdls/product_bff_search.wsdl").toURI().toURL()

        val servicePort = OrderService(wsdlFileURL).orderServicePort
        val bindingProvider = servicePort as BindingProvider
        bindingProvider.requestContext[BindingProvider.ENDPOINT_ADDRESS_PROPERTY] = "http://localhost:8080/ws"
        val response = servicePort.createOrder(orderRequest)

        assertThat(response.id).isEqualTo(10)
    }
}