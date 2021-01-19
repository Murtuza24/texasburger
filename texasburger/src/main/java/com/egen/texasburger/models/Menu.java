package com.egen.texasburger.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Murtuza
 */

@Document(collection = "menu")
@Data
public class Menu {

    @Id
    private String menuId;
    private String restaurantId_fk;
    private String menuType;
    private List<MenuItem> menuItems;


}
