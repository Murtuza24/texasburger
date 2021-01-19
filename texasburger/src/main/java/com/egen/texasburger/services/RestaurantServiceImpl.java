package com.egen.texasburger.services;

import com.egen.texasburger.models.Restaurant;
import com.egen.texasburger.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

@Service(value = "restaurantService")
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> getRestaurantById(String restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    @Override
    public List<Restaurant> getRestaurantByCity(String city) {
        return restaurantRepository.findByCity(city);
    }
}
