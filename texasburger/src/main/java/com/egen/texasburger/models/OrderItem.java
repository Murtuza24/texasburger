package com.egen.texasburger.models;

import lombok.Data;

@Data
public class OrderItem {

    private String orderItemId;
    private String orderItemName;
    private Double itemPrice;
    private int quantity;

}
