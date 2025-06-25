package com.tennis.controller;

import com.tennis.model.Order;
import com.tennis.model.OrderStatus;
import com.tennis.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		Order savedOrder = orderService.createOrder(order);
		return ResponseEntity.status(201).body(savedOrder);
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		List<Order> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		Order order = orderService.getOrderById(id);
		return ResponseEntity.ok(order);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
		orderService.updateOrderStatus(id, status);
		Order updatedOrder = orderService.getOrderById(id);
		return ResponseEntity.ok(updatedOrder);
	}

	@GetMapping("/{id}/status")
	public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable Long id) {
		OrderStatus status = orderService.getOrderStatus(id);
		return ResponseEntity.ok(status);
	}
}