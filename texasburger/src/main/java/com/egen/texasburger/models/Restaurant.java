package com.egen.texasburger.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurant")
@Data
public class Restaurant {
    @Id
    private String restaurantId;
    private String restaurantName;
    private String openTime;
    private String closeTime;
    private Address address;
}
