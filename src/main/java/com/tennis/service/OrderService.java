package com.tennis.service;

import com.tennis.exception.ResourceNotFoundException;
import com.tennis.model.Order;
import com.tennis.model.OrderStatus;
import com.tennis.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public Order createOrder(Order order) {
		return orderRepository.save(order);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Order getOrderById(Long id) {
		return findOrderByIdOrThrow(id);
	}

	public void deleteOrder(Long id) {
		if (!orderRepository.existsById(id)) {
			throw new ResourceNotFoundException("Order not found with ID: " + id);
		}
		orderRepository.deleteById(id);
	}

	public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
		Order order = findOrderByIdOrThrow(orderId);

		if (order.getStatus() == newStatus) {
			throw new IllegalStateException("Order is already in the desired status.");
		}

		order.setStatus(newStatus);
		return orderRepository.save(order);
	}

	public OrderStatus getOrderStatus(Long id) {
		return findOrderByIdOrThrow(id).getStatus();
	}

	private Order findOrderByIdOrThrow(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
	}
}