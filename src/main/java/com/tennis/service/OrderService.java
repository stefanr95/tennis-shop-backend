package com.tennis.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.tennis.exception.ResourceNotFoundException;
import com.tennis.model.Order;
import com.tennis.model.OrderStatus;
import com.tennis.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	// Kreiranje nove porudžbine
	public Order createOrder(Order order) {
		return orderRepository.save(order);
	}

	// Dohvatanje svih porudžbina
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	// Dohvatanje porudžbine po ID-u
	public Order getOrderById(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
	}

	// Brisanje porudžbine
	public void deleteOrder(Long id) {
		orderRepository.deleteById(id);
	}

	// Ažuriranje statusa porudžbine
	public void updateOrderStatus(Long orderId, OrderStatus status) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

		if (order.getStatus() != status) {
			order.setStatus(status);
			orderRepository.save(order);
		} else {
			throw new IllegalStateException("Order is already in the desired status.");
		}
	}

	// Dohvat statusa porudžbine
	public OrderStatus getOrderStatus(Long id) {
		Order order = getOrderById(id);
		return order.getStatus();
	}
}