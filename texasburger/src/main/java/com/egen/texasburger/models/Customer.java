package com.egen.texasburger.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer")
@Data
public class Customer {
    @Id
    private Long customerId;
    private String customerName;
    private Address address;

}
