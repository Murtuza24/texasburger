package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Murtuza
 */

public interface OrderRepository extends MongoRepository<Order, String> {
}
