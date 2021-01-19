package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author Murtuza
 */
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    @Query(value = "{address.city:?0}")
    List<Restaurant> findByCity(String city);
}
