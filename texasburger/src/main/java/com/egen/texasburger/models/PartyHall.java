package com.egen.texasburger.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Murtuza
 */

@Document(collection = "partyhall")
@Data
public class PartyHall {

    private String restaurantId;
    private List<PartyTables> partyTablesList;
    private String status;

}
