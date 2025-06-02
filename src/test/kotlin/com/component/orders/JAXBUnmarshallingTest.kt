package com.component.orders

import com.example.orders.OrderRequest
import com.component.orders.config.WebServiceConfig
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Unmarshaller
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import java.io.StringReader
import javax.xml.transform.stream.StreamSource

class JAXBUnmarshallingTest {

    private fun createUnmarshaller(): Unmarshaller {
        val jaxbContext = JAXBContext.newInstance(OrderRequest::class.java)
        return jaxbContext.createUnmarshaller()
    }

    private fun createSpringMarshaller(): Jaxb2Marshaller {
        val config = WebServiceConfig()
        return config.marshaller()
    }

    @Test
    fun `should unmarshal OrderRequest with default namespace declaration`() {
        // This is the actual format from the SOAP payload - the main test case
        val xmlWithDefaultNamespace = """
            <OrderRequest xmlns="http://www.example.com/orders">
                <productid>688</productid>
                <count>126</count>
            </OrderRequest>
        """.trimIndent()

        val unmarshaller = createUnmarshaller()
        val orderRequest = unmarshaller.unmarshal(StringReader(xmlWithDefaultNamespace)) as OrderRequest

        // This should now work with elementFormDefault="qualified" in WSDL
        assertEquals(688, orderRequest.productid)
        assertEquals(126, orderRequest.count)
    }

    @Test
    fun `should unmarshal OrderRequest with default namespace using Spring marshaller`() {
        // Test with Spring marshaller configuration
        val xmlWithDefaultNamespace = """
            <OrderRequest xmlns="http://www.example.com/orders">
                <productid>688</productid>
                <count>126</count>
            </OrderRequest>
        """.trimIndent()

        val marshaller = createSpringMarshaller()
        val source = StreamSource(StringReader(xmlWithDefaultNamespace))
        val orderRequest = marshaller.unmarshal(source) as OrderRequest

        assertEquals(688, orderRequest.productid)
        assertEquals(126, orderRequest.count)
    }

    @Test
    fun `should unmarshal OrderRequest with explicit namespace prefix`() {
        // This format with explicit prefix should also work
        val xmlWithNamespacePrefix = """
            <tns:OrderRequest xmlns:tns="http://www.example.com/orders">
                <tns:productid>688</tns:productid>
                <tns:count>126</tns:count>
            </tns:OrderRequest>
        """.trimIndent()

        val unmarshaller = createUnmarshaller()
        val orderRequest = unmarshaller.unmarshal(StringReader(xmlWithNamespacePrefix)) as OrderRequest

        assertEquals(688, orderRequest.productid)
        assertEquals(126, orderRequest.count)
    }
}