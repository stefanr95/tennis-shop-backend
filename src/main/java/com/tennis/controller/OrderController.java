package com.tennis.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tennis.model.Order;
import com.tennis.model.OrderStatus;
import com.tennis.service.OrderService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	// Kreiranje porudzbine
	@PostMapping("/checkout")
	public ResponseEntity<Order> checkout(@RequestBody Order order) {
		Order saved = orderService.createOrder(order);
		return ResponseEntity.status(201).body(saved);
	}

	// Dohvatanje svih porudzbina
	@GetMapping
	public List<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	// Detalji pojedinačne porudžbine
	@GetMapping("/{id}")
	public Order getOrderById(@PathVariable Long id) {
		return orderService.getOrderById(id);
	}

	// Brisanje porudžbine
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.status(204).build();
	}

	// Ažuriranje statusa porudžbine
	@PutMapping("/update-status/{id}")
	public Order updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
		orderService.updateOrderStatus(id, status);
		return orderService.getOrderById(id);
	}

	// Dohvat statusa porudžbine
	@GetMapping("/status/{id}")
	public OrderStatus getOrderStatus(@PathVariable Long id) {
		return orderService.getOrderStatus(id);
	}
}