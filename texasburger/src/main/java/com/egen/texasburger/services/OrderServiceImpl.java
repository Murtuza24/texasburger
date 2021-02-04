package com.egen.texasburger.services;

import com.egen.texasburger.models.Order;
import com.egen.texasburger.models.OrderStatus;
import com.egen.texasburger.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

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
    public List<Order> getAllOrders(String restaurantId) {
        return orderRepository.findOrdersByRestaurantId(restaurantId);
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Order placeOrder(Order order) {
        order.setStatus(OrderStatus.ORDER_PLACED.name());
        TimeZone tz = TimeZone.getTimeZone("America/Chicago");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        order.setCreatedTime(nowAsISO);

        return orderRepository.save(order);
    }

    @Override
    public String cancelOrder(String orderId) {
        Optional<Order> _order = getOrderById(orderId);
        if (_order.isPresent()) {
            if (_order.get().getStatus().equals(OrderStatus.ORDER_PLACED.name())) {
                orderRepository.deleteById(orderId);
                return "order cancelled";
            } else {
                return "order cannot be cancelled now";
            }
        } else {
            return "order does not exist!";
        }
    }

    @Override
    public Order updateOrder(String orderId, Order order) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent() && optionalOrder.get().getOrderId() != null
                && !optionalOrder.get().getOrderItemList().isEmpty()
                && !optionalOrder.get().getStatus().equalsIgnoreCase(OrderStatus.DELIVERED.name())) {
            // since already exists it will update rather than inserting a new entry
            return orderRepository.save(order);
        }
        return null;
    }


}
