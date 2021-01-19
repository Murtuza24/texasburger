package com.egen.texasburger.services;

import com.egen.texasburger.models.Restaurant;

import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

public interface RestaurantService {

    List<Restaurant> getAllRestaurants();

    Optional<Restaurant> getRestaurantById(String restaurantId);

    List<Restaurant> getRestaurantByCity(String city);

}
