package com.egen.texasburger.services;

import com.egen.texasburger.models.Order;

import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

public interface OrderService {

    List<Order> getAllOrders();

    Optional<Order> getOrderById(String orderId);

    Order placeOrder(Order order);

}
