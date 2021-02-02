package com.egen.texasburger.models;

import lombok.Data;

@Data
public class Address {

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipcode;
    private Double latitude;
    private Double longitude;


}
