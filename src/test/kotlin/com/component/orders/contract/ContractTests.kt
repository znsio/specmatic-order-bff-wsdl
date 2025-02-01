package com.component.orders.contract

import io.specmatic.stub.ContractStub
import io.specmatic.stub.createStub
import io.specmatic.test.SpecmaticContractTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContractTests : SpecmaticContractTest {

    companion object {
        private var httpStub: ContractStub? = null
        private const val APPLICATION_HOST = "localhost"
        private const val APPLICATION_PORT = "8080"
        private const val HTTP_STUB_HOST = "localhost"
        private const val HTTP_STUB_PORT = 8090
        private const val ACTUATOR_MAPPINGS_ENDPOINT =
            "http://$APPLICATION_HOST:$APPLICATION_PORT/actuator/mappings"

        @JvmStatic
        @BeforeAll
        fun setUp() {
            System.setProperty("host", APPLICATION_HOST)
            System.setProperty("port", APPLICATION_PORT)
            System.setProperty("endpointsAPI", ACTUATOR_MAPPINGS_ENDPOINT)

//            httpStub = createStub(HTTP_STUB_HOST, HTTP_STUB_PORT)

            httpStub = createStub(
                host = HTTP_STUB_HOST,
                port = HTTP_STUB_PORT,
                strict = true)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            httpStub?.close()
        }
    }
}

