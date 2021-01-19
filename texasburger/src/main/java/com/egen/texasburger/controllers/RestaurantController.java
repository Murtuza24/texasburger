package com.egen.texasburger.controllers;

import com.egen.texasburger.exception.CustomException;
import com.egen.texasburger.models.Restaurant;
import com.egen.texasburger.services.RestaurantServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

@RestController
@RequestMapping(value = "/api/restaurants")
@Log4j2
public class RestaurantController {

    @Resource(name = "restaurantService")
    private RestaurantServiceImpl restaurantService;


    @GetMapping(value = "/")
    public ResponseEntity<?> getAllRestaurants() {
        List<Restaurant> restaurantList = restaurantService.getAllRestaurants();
        if (restaurantList.isEmpty()) {
            log.info("Restaurant document is empty");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Total Restaurants: " + restaurantList.size());
            restaurantList.forEach(System.out::println);
            return new ResponseEntity<>(restaurantList, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{restaurantId}")
    public ResponseEntity<?> getRestaurantById(@NotNull(message = "rest id cannot be null")
                                               @PathVariable("restaurantId") String restaurantId) {
        Optional<?> restaurant = restaurantService.getRestaurantById(restaurantId);
        if (!restaurant.isPresent()) {
            log.info(String.format("Restaurant Id %s Not Found", restaurantId));
            throw new CustomException("No Such Restaurant Found");
        } else {
            log.info(String.format("Restaurant Id %s found", restaurantId));
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/city/{city}")
    public ResponseEntity<?> getRestaurantByCity(@NotNull(message = "param cannot be null")
                                                 @PathVariable("city") String city) {
        List<Restaurant> restaurantList = restaurantService.getRestaurantByCity(city);

        if (restaurantList.isEmpty()) {
            log.info("No Restaurant in City: ");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info(String.format("Total Restaurants in %s is %s: ", city, restaurantList.size()));
            restaurantList.forEach(System.out::println);
            return new ResponseEntity<>(restaurantList, HttpStatus.OK);
        }

    }


}
