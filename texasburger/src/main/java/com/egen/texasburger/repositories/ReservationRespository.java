package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * @author Murtuza
 */

public interface ReservationRespository extends MongoRepository<Reservation, String> {

    @Query(value = "{'restaurantId':?0, 'dateTime': {'$gte':?1} }")
    Page<Reservation> findByDate(String restaurant, String date, Pageable pageable);

    @Query(value = "{'restaurantId':?0, 'phone':?1}")
    Optional<Reservation> findByPhone(String restaurantId, String phone);

}
