package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author Murtuza
 */

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    @Query(value = "{'address.city':?0}")
    Page<Restaurant> findByCity(String city, Pageable pageable);

    @Query(value = "{'address.state':?0}")
    Page<Restaurant> findByState(String state, Pageable pageable);

    @Query(value = "{'restaurantId':?0}")
    Restaurant findByResId(String restaurantId);

}
