package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Murtuza
 */

public interface OrderRepository extends MongoRepository<Order, String> {
}
