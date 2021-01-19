package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Murtuza
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
