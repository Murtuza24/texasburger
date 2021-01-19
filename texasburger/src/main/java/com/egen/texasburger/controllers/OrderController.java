package com.egen.texasburger.controllers;

import com.egen.texasburger.exception.CustomException;
import com.egen.texasburger.models.Order;
import com.egen.texasburger.services.OrderServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

@RestController
@RequestMapping("/api/orders")
@Log4j2
public class OrderController {

    @Resource(name = "orderService")
    private OrderServiceImpl orderService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();
        if (orderList.isEmpty()) {
            log.info("No Orders Found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Total Orders: " + orderList.size());
            orderList.forEach(System.out::println);
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable("orderId") String orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        if (!order.isPresent()) {
            log.info(String.format("OrderId %s Not Found", orderId));
            throw new CustomException("Order Not Found");
        } else {
            return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/placeOrder", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> placeOrder(@NotNull(message = "Order object cannot be null")
                                            @RequestBody Order order) {
        if (order.getOrderItemList().isEmpty()) {
            log.info("Order item list cannot be empty");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (order.getStatus().equalsIgnoreCase("PAYMENT_ACCEPTED")) {
            try {
                Order orderPlaced = orderService.placeOrder(order);
                log.info("Order Placed! Order Id: " + orderPlaced.getOrderId());
                log.info("Order Status: " + orderPlaced.getStatus());
                return new ResponseEntity<>(orderPlaced, HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("error placing order: " + e.getMessage());
            }

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
