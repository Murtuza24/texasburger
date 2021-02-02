package com.egen.texasburger.services;

import com.egen.texasburger.models.Reservation;
import com.egen.texasburger.repositories.ReservationRespository;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;


/**
 * @author Murtuza
 */

@Service(value = "reservationService")
public class ReservationService {

    @Autowired
    ReservationRespository reservationRespository;

    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRespository.findAll(pageable);
    }

    public Page<Reservation> getByDate(String restaurantId, String date, Pageable pageable) {
        DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date, parser);
        DateTimeFormatter printer = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.S");
        String formatted = printer.print(localDateTime);
        System.out.println("ISO DATE FORMAT: " + formatted);
        return reservationRespository.findByDate(restaurantId, formatted, pageable);
    }

    public Optional<Reservation> getReservationById(String reservationId) {
        return reservationRespository.findById(reservationId);
    }

    public Optional<Reservation> getReservationByPhone(String restaurantId, String phone) {
        return reservationRespository.findByPhone(restaurantId, phone);
    }

    public Reservation createReservation(Reservation reservation) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        reservation.setCreatedOn(nowAsISO);
        return reservationRespository.save(reservation);
    }

}
