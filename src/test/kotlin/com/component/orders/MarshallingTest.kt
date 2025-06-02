package com.component.orders

import com.example.orders.OrderRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class MarshallingTest {

    @Test
    fun `should properly unmarshal OrderRequest with productid and count`() {
        val marshaller = Jaxb2Marshaller()
        marshaller.setContextPath("com.example.orders")
        marshaller.afterPropertiesSet()

        // Let's first test creating an object manually and marshalling it
        val manualOrder = OrderRequest()
        manualOrder.productid = 123
        manualOrder.count = 456
        
        val writer = StringWriter()
        marshaller.marshal(manualOrder, StreamResult(writer))
        println("Manual object marshalled: ${writer.toString()}")

        // Now test the original unmarshalling
        val xml = """
            <OrderRequest xmlns="http://www.example.com/orders">
                <productid>688</productid>
                <count>126</count>
            </OrderRequest>
        """.trimIndent()

        println("Input XML: $xml")
        val source = StreamSource(StringReader(xml))
        val result = marshaller.unmarshal(source) as OrderRequest

        println("OrderRequest object: $result")
        println("productid: ${result.productid}")
        println("count: ${result.count}")

        assertEquals(688, result.productid)
        assertEquals(126, result.count)
    }
}