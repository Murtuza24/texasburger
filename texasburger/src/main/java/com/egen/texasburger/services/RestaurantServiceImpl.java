package com.egen.texasburger.services;

import com.egen.texasburger.models.Restaurant;
import com.egen.texasburger.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

@Service(value = "restaurantService")
public class RestaurantServiceImpl {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // return a list of all restaurants
    public Page<Restaurant> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    // returns a restaurant object if exists
    public Optional<Restaurant> findByResId(String restaurantId) {
        return Optional.ofNullable(restaurantRepository.findByResId(restaurantId));
    }

    // returns list of restaurants in the city
    public Page<Restaurant> getRestaurantByCity(String city, Pageable pageable) {
        return restaurantRepository.findByCity(city, pageable);
    }

    // returns list of restaurants in the state
    public Page<Restaurant> getRestaurantByState(String state, Pageable pageable) {
        return restaurantRepository.findByState(state, pageable);
    }

    public Restaurant addNewRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> addMultipleRestaurants(Iterable<Restaurant> restaurant) {
        return restaurantRepository.saveAll(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        restaurantRepository.delete(restaurant);
    }

    public void deleteAllRestaurant() {
        restaurantRepository.deleteAll();
    }


}
