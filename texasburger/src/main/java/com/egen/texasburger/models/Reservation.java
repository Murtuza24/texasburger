package com.egen.texasburger.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

/**
 * @author Murtuza
 */
@Document(collection = "reservation")
@Data
public class Reservation {

    @Id
    private String reservationId;
    @NotEmpty
    private String restaurantId;
    private String customerId;
    @NotEmpty
    private String customerName;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String dateTime;
    @NotEmpty
    private String createdOn;
    private String orderId;
    private Double reservationCharges;
}
