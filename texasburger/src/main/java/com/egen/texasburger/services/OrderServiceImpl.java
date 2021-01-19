package com.egen.texasburger.services;

import com.egen.texasburger.models.Order;
import com.egen.texasburger.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Order placeOrder(Order order) {
        order.setStatus("ORDER_PLACED");
        return orderRepository.save(order);
    }


}
