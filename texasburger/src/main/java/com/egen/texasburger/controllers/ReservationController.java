package com.egen.texasburger.controllers;

import com.egen.texasburger.exception.CustomException;
import com.egen.texasburger.models.Reservation;
import com.egen.texasburger.services.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Murtuza
 */

@RestController
@RestControllerAdvice
@RequestMapping(value = "/api")
@Log4j2
@Api(value = "Reservations API")
public class ReservationController {

    @Resource(name = "reservationService")
    private ReservationService reservationService;

    @GetMapping(value = "/reservations")
    @ApiOperation(value = "Get All Reservations", notes = "Date format required: yyyy-MM-dd HH:mm:ss.S")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Map<String, Object>> getReservations(@RequestParam(required = false) String restaurantId,
                                                               @RequestParam(required = false) String date,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size) {
        try {
            List<Reservation> reservationList;
            Pageable paging = PageRequest.of(page, size);
            Page<Reservation> reservationPage = null;

            if (date == null) {
                log.info("getting all reservations");
                reservationPage = reservationService.getAllReservations(paging);
            } else {
                log.info("getting reservations by date");
                reservationPage = reservationService.getByDate(restaurantId, date, paging);
            }
            reservationList = reservationPage.getContent();
            if (reservationList.isEmpty()) {
                log.info(String.format("No Reservations on %s: ", date));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.info(String.format("Total Reservations %s: ", reservationList.size()));

                Map<String, Object> response = new HashMap<>();
                response.put("reservations", reservationList);
                response.put("currentPage", reservationPage.getNumber());
                response.put("totalItems", reservationPage.getTotalElements());
                response.put("totalPages", reservationPage.getTotalPages());

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("error getting reservations: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/reservations/id/{reservationId}")
    @ApiOperation(value = "Get Reservation by Id", notes = "Pass reservation id in path.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Optional<Reservation>> getReservationById(
            @PathVariable(name = "reservationId") String reservationId) {
        try {
            Optional<Reservation> reservation = reservationService.getReservationById(reservationId);

            if (reservation.isPresent()) {
                log.info("reservation by id {} found", reservationId);
                return new ResponseEntity<>(reservation, HttpStatus.OK);
            } else {
                log.info("No reservation found with id {}", reservationId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.error("error finding reservation by id: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/reservation")
    @ApiOperation(value = "Get Reservation by Id", notes = "Pass restaurant id and phone")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Optional<Reservation>> getReservationByPhone(
            @RequestParam(value = "restaurantId") String restaurantId,
            @RequestParam(value = "phone") String phone) {
        try {
            Optional<Reservation> reservation = reservationService.getReservationByPhone(restaurantId, phone);

            if (reservation.isPresent()) {
                log.info("reservation by phone {} found", phone);
                return new ResponseEntity<>(reservation, HttpStatus.OK);
            } else {
                log.info("No reservation found with phone {}", phone);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.error("error finding reservation by id: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping(value = "/reservations/createReservation", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Reservation", notes = "Phone, Name, dateTime(ISO format)")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 402, message = "Payment Required"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Reservation> createReservation(@NotNull(message = "reservation object cannot be null")
                                                         @RequestBody Reservation reservation) {
        try {
            if (reservation.getRestaurantId() == null || reservation.getRestaurantId().equals("") ||
                    reservation.getPhone() == null || reservation.getPhone().equals("") ||
                    reservation.getCustomerName() == null || reservation.getCustomerName().equals("")
            ) {
                log.info("restauantId needed to create reservation");
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            } else {
                Reservation _reservation = reservationService.createReservation(reservation);
                if (_reservation.getReservationId() != null) {
                    log.info("reservation created. Id: {}", _reservation.getReservationId());
                    return new ResponseEntity<>(_reservation, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            log.info("error creating reservation: {}", e.getMessage());
            throw new CustomException("Internal server error");
        }
    }

    // delete reservation

    //delete reservations


}
