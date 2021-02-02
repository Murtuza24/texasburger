package com.egen.texasburger.models;

import lombok.Data;

/**
 * @author Murtuza
 */

@Data
public class DailyHours {

    private String day;
    private String openHours;
    private String closeHours;
}
