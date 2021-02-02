package com.egen.texasburger.models;

import lombok.Data;


/**
 * @author Murtuza
 */

@Data
public class MenuItem {
    private String itemId;
    private String itemName;
    private String itemDescription;
    private Double itemPrice;


}
