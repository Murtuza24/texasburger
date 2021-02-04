package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Murtuza
 */

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query(value = "{'restaurantId':?0}")
    List<Order> findOrdersByRestaurantId(String restaurantId);

}
