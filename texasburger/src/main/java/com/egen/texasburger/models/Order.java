package com.egen.texasburger.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "order")
@Data
public class Order {
    @Id
    private String orderId;
    private String customerId;
    private Double totalPrice;
    private String status;
    private String createdTime;
    private List<OrderItem> orderItemList;
}
