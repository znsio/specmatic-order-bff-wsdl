# WSDL Java Sample Project

## Architecture

### Production Setup

```
+-------------------+         +--------------------------+         +-------------------+
|                   |         |                          |         |                   |
|  WSDL SOAP Client |         |  Product BFF Search      |         |  order_api        |
|                   |         |  Service (BFF)           |         |  (WSDL Service)   |
+---------+---------+         +-----------+--------------+         +---------+---------+
          |                               |                                  |
          | 1. Request (SOAP)             |                                  |
          +------------------------------>|                                  |
          |                               |                                  |
          |                               | 2. Request (SOAP)                |
          |                               +--------------------------------->|
          |                               |                                  |
          |                               |                3. Response (SOAP)|
          |                               |<---------------------------------+
          | 4. Response (SOAP)            |                                  |
          +<------------------------------+                                  |
```

### Contract Test Setup

```
+-------------------------+         +--------------------------+         +----------------------+
|                         |         |                          |         |                      |
| Specmatic Contract Test |         |  Product BFF Search      |         |  Specmatic Mock      |
|   (WSDL of BFF)         |         |  Service (BFF)           |         |  (WSDL of order_api) |
+-----------+-------------+         +-----------+--------------+         +----------+-----------+
            |                                   |                                   |
            | 1. Request (SOAP)                 |                                   |
            +---------------------------------->|                                   |
            |                                   |                                   |
            |                                   | 2. Request (SOAP)                 |
            |                                   +---------------------------------->|
            |                                   |                                   |
            |                                   |                3. Response (SOAP) |
            |                                   |<----------------------------------+
            | 4. Response (SOAP)                |                                   |
            +<----------------------------------+                                   |
            |                          
            |<-------------------+
            | 5. Assert response |
            | against WSDL spec  |
            +--------------------+
```

## Running Contract Test and Mock Programmatically

Please refer to [ContractTest.kt](src/test/kotlin/com/component/orders/ContractTest.kt)

Once you run the test the report should be available at `build/reports/specmatic/html/index.html`.

## Running the Specmatic WSDL Mock Server from Command Line using Docker

```shell
docker run -p "8090:9000" -v "$(pwd):/usr/src/app" znsio/specmatic stub "./wsdls/order_api.wsdl"
```

OR

```shell
java -jar specmatic.jar stub --port 8090 ./wsdls/order_api.wsdl
```

Now we can verify if the stub server is running
1. Open SOAPUI
2. Import the wsdl in `wsdls/order_api.wsdl`
3. Remember to set the endpoint URL to host `localhost` and port `8090`
4. Send the following request using SOAPUI:

```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns0:CreateOrder xmlns:ns0="http://www.example.com/orders">
            <productid>123</productid>
            <count>1</count>
        </ns0:CreateOrder>
    </S:Body>
</S:Envelope>
```

And you should get a response like this:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <soapenv:Header/>
    <soapenv:Body>
        <OrderId>
            <id>773</id>
        </OrderId>
    </soapenv:Body>
</soapenv:Envelope>
```

Alternatively, you can also run the same WSDL spec as a test against the mock server.

```shell
docker run --net host -v "$(pwd):/usr/src/app" znsio/specmatic test "./wsdls/order_api.wsdl" --port 8090
```

The HTML API Coverage Report will be available at `build/reports/specmatic/html/index.html`.
