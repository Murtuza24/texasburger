package com.egen.texasburger.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "restaurants")
@Data
public class Restaurant {
    @Id
    private String restaurantId;
    private String restaurantName;
    private String phone;
    private String email;
    private List<DailyHours> dailyHours;
    private Address address;
}
