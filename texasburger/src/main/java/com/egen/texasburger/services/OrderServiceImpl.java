package com.egen.texasburger.services;

import com.egen.texasburger.models.Order;
import com.egen.texasburger.models.OrderStatus;
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

    @Override
    public String cancelOrder(String orderId) {
        Optional<Order> _order = getOrderById(orderId);
        if (_order.isPresent()) {
            if (_order.get().getStatus().equals(OrderStatus.ORDER_PLACED.name())) {
                orderRepository.deleteById(orderId);
                return "cancelled";
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
