#!/bin/bash

# Script to fix namespace issues in generated JAXB classes
cd "$(dirname "$0")"

# Fix OrderRequest.java to handle default namespace properly
ORDERREQUEST_FILE="src/generated/wsdl2java/com/example/orders/OrderRequest.java"

if [ -f "$ORDERREQUEST_FILE" ]; then
    echo "Fixing namespace handling in OrderRequest.java"
    
    # Add XmlElement import if not present
    if ! grep -q "import jakarta.xml.bind.annotation.XmlElement;" "$ORDERREQUEST_FILE"; then
        sed -i '/import jakarta.xml.bind.annotation.XmlType;/a import jakarta.xml.bind.annotation.XmlElement;' "$ORDERREQUEST_FILE"
    fi
    
    # Add namespace annotation to productid field
    sed -i 's/protected int productid;/@XmlElement(namespace = "http:\/\/www.example.com\/orders")\n    protected int productid;/' "$ORDERREQUEST_FILE"
    
    # Add namespace annotation to count field  
    sed -i 's/protected int count;/@XmlElement(namespace = "http:\/\/www.example.com\/orders")\n    protected int count;/' "$ORDERREQUEST_FILE"
    
    echo "Fixed OrderRequest.java namespace annotations"
else
    echo "OrderRequest.java not found at $ORDERREQUEST_FILE"
fi