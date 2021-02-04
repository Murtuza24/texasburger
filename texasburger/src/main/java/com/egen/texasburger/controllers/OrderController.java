package com.egen.texasburger.controllers;

import com.egen.texasburger.exception.CustomException;
import com.egen.texasburger.models.Order;
import com.egen.texasburger.models.OrderStatus;
import com.egen.texasburger.services.OrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping("/api")
@Log4j2
@Api(value = "Hamburger Orders API")
public class OrderController {

    @Resource(name = "orderService")
    private OrderServiceImpl orderService;

    @GetMapping(value = "/orders")
    @ApiOperation(value = "Get All Orders", notes = "Get All Orders/Orders by restaurantId")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(name = "restaurantId", required = false) String restaurantId) {
        List<Order> orderList;
        if (restaurantId == null || restaurantId.isEmpty()) {
            orderList = orderService.getAllOrders();
        } else {
            orderList = orderService.getAllOrders(restaurantId);
        }
        if (orderList.isEmpty()) {
            log.info("No Orders Found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Total Orders: " + orderList.size());
//            orderList.forEach(System.out::println);
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/orders/{orderId}")
    @ApiOperation(value = "Get order by Id", notes = "Get order by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable(name = "orderId") String orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        if (!order.isPresent()) {
            log.info(String.format("OrderId %s Not Found", orderId));
            throw new CustomException("Order Not Found");
        } else {
            return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
        }
    }

    // validate order price before placing
    @PostMapping(value = "/orders/placeOrder", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Place Order", notes = "Place a new order. Must contain atleast one item.")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 402, message = "Payment Required"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Order> placeOrder(@NotNull(message = "Order object cannot be null")
                                            @RequestBody Order order) {
        if (order.getOrderItemList().isEmpty() || order.getRestaurantId() == null
                || order.getRestaurantId().equals("")) {
            log.info("need restaurant id and order items to place order");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (order.getStatus().equalsIgnoreCase(OrderStatus.PAYMENT_ACCEPTED.name())
                || order.getStatus().equalsIgnoreCase(OrderStatus.DINE_IN.name())
        ) {
            try {
                Order orderPlaced = orderService.placeOrder(order);
                log.info("Order Placed! Order Id: " + orderPlaced.getOrderId());
                log.info("Order Status: " + orderPlaced.getStatus());
                return new ResponseEntity<>(orderPlaced, HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("error placing order: " + e.getMessage());
            }
        } else {
            log.info("Payment not completed!!");
            return new ResponseEntity<>(HttpStatus.PAYMENT_REQUIRED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/orders/updateOrder/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Place Order", notes = "Place a new order. Must contain atleast one item.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Order> updateOrder(@PathVariable(name = "orderId") @NotNull String orderId,
                                             @RequestBody Order order) {

        try {
            if (orderId == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            } else if (order != null) {
                return new ResponseEntity<>(orderService.updateOrder(orderId, order), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    // cancel order
    @DeleteMapping(value = "/orders/cancelOrder/{orderId}")
    @ApiOperation(value = "Cancel Order", notes = "Must contain orderId.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable(name = "orderId") @NotNull String orderId) {

        Map<String, Object> resp = new HashMap<>();


        if (orderId != null) {
            String msg = orderService.cancelOrder(orderId);
            resp.put("message", msg);
            resp.put("status", HttpStatus.OK);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } else {
            resp.put("message", "No such order");
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }
    }

}
