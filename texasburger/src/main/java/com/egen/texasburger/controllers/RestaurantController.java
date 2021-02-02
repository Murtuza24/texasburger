package com.egen.texasburger.controllers;

import com.egen.texasburger.models.Restaurant;
import com.egen.texasburger.services.RestaurantServiceImpl;
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
@Api(value = "Hamburger Restaurant/Location API")
public class RestaurantController {

    @Resource(name = "restaurantService")
    private RestaurantServiceImpl restaurantService;

    @GetMapping(value = "/restaurants")
    @ApiOperation(value = "Filter Restaurants by location.", notes = "Filter Restaurants by location.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Map<String, Object>> getRestaurantByFilter(@RequestParam(value = "filterBy", required = false) String filterBy,
                                                                     @RequestParam(value = "value", required = false) String value,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int size
    ) {
        try {
            List<Restaurant> restaurantList;
            Pageable paging = PageRequest.of(page, size);
            Page<Restaurant> pageRestaurants = null;

            if (filterBy == null || value == null) {
                pageRestaurants = restaurantService.getAllRestaurants(paging);
            } else if (filterBy.equals("state")) {
                pageRestaurants = restaurantService.getRestaurantByState(value, paging);
            } else if (filterBy.equals("city")) {
                pageRestaurants = restaurantService.getRestaurantByCity(value, paging);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            restaurantList = pageRestaurants.getContent();
            if (restaurantList.isEmpty()) {
                log.info(String.format("No Restaurants in %s of %s: ", filterBy, value));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.info(String.format("Total Restaurants in %s is %s: ", value, restaurantList.size()));
//            restaurantList.forEach(System.out::println);

                Map<String, Object> response = new HashMap<>();
                response.put("restaurants", restaurantList);
                response.put("currentPage", pageRestaurants.getNumber());
                response.put("totalItems", pageRestaurants.getTotalElements());
                response.put("totalPages", pageRestaurants.getTotalPages());

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping(value = "/restaurants/{restaurantId}")
    @ApiOperation(value = "Get Restaurant by Id", notes = "Get Restaurant by Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Optional<Restaurant>> getRestaurantById(@NotNull(message = "rest id cannot be null")
                                                                  @PathVariable("restaurantId") String restaurantId) {
        try {
            Optional<Restaurant> restaurant = restaurantService.findByResId(restaurantId);
            if (!restaurant.isPresent()) {
                log.info(String.format("Restaurant Id %s Not Found", restaurantId));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.info(String.format("Restaurant Id %s found", restaurantId));
                return new ResponseEntity<>(restaurant, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/admin/add/NewRestaurant", consumes = "application/json",
            produces = "application/json")
    @ApiOperation(value = "Add new Restaurant", notes = "Add New Restaurant")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Restaurant> addNewRestaurant(@NotNull(message = "restaurant object cannot be null")
                                                       @RequestBody Restaurant restaurant) {
        try {
            if (restaurant.getRestaurantName() != null && restaurant.getAddress() != null) {
                log.info(String.format("Restaurant city: %s", restaurant.getAddress().getCity()));
                Restaurant respRes = restaurantService.addNewRestaurant(restaurant);
                log.info(String.format("Restaurant created successfully %s", respRes.getRestaurantId()));
                return new ResponseEntity<>(respRes, HttpStatus.CREATED);
            } else {
                log.info("error creating new restaurant {}", restaurant);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(String.format("Error creating restaurant: %s", e.getMessage()));
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    @PostMapping(value = "/admin/add/MultipleRestaurants", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add new Multiple restaurants", notes = "Add new Multiple restaurants")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 304, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<List<Restaurant>> addMultipleRestaurants(@NotNull(message = "restaurant object cannot be null")
                                                                   @RequestBody List<Restaurant> restaurants) {
        try {
            if (!restaurants.isEmpty()) {
                List<Restaurant> respRes = restaurantService.addMultipleRestaurants(restaurants);
                log.info(String.format("Multiple Restaurants created successfully: %s", respRes.size()));
                return new ResponseEntity<>(respRes, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(String.format("Error creating multiple restaurants: %s", e.getMessage()));
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping(value = "/admin/update/Restaurant/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter Restaurants by location.", notes = "Filter Restaurants by location.")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable("restaurantId") String restaurantId,
                                                       @RequestBody Restaurant restaurant) {
        try {
            Optional<Restaurant> restaurantData = restaurantService.findByResId(restaurantId);

            if (restaurantData.isPresent()) {
                Restaurant _restaurant = restaurantData.get();
                _restaurant.setRestaurantName(restaurant.getRestaurantName());
                _restaurant.setDailyHours(restaurant.getDailyHours());
                _restaurant.setEmail(restaurant.getEmail());
                _restaurant.setPhone(restaurant.getPhone());
                _restaurant.setAddress(restaurant.getAddress());
                log.info("restaurant location/details updated");
                return new ResponseEntity<>(restaurantService.addNewRestaurant(_restaurant), HttpStatus.OK);
            } else {
                log.error("restaurant not found/failed to update the details!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.info("error updating restaurant: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/admin/remove/Restaurant", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Filter Restaurants by location.", notes = "Filter Restaurants by location.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<HttpStatus> removeRestaurant(@RequestBody Restaurant restaurant) {
        try {
            restaurantService.deleteRestaurant(restaurant);
            log.info(String.format("Restaurant %s deleted successfully!", restaurant.getRestaurantId()));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error(String.format("error in deleting restaurant %s: %s", restaurant.getRestaurantId(),
                    e.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/admin/removeAll/Restaurants")
    @ApiOperation(value = "Remove all restaurants", notes = "Remove all restaurants.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<HttpStatus> removeAllRestaurants() {
        try {
            restaurantService.deleteAllRestaurant();
            log.info("All Restaurants %s deleted successfully!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error(String.format("Error in deleting all restaurants: %s", e.getMessage()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
