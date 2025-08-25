package com.tennis.controller;

import com.tennis.model.CartItem;
import com.tennis.model.Order;
import com.tennis.model.OrderStatus;
import com.tennis.model.User;
import com.tennis.repository.CartItemRepository;
import com.tennis.repository.OrderRepository;
import com.tennis.service.OrderService;
import com.tennis.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

	private final OrderService orderService;
	private final UserService userService;
	private final OrderRepository orderRepository;
	private final CartItemRepository cartItemRepository;

	public OrderController(OrderService orderService, UserService userService, OrderRepository orderRepository,
			CartItemRepository cartItemRepository) {
		this.orderService = orderService;
		this.userService = userService;
		this.orderRepository = orderRepository;
		this.cartItemRepository = cartItemRepository;
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

	@PostMapping("/checkout")
	public ResponseEntity<Order> checkout(@RequestBody Order request, Principal principal) {
		if (principal == null) {
			return ResponseEntity.status(401).build();
		}

		User user = userService.findByUsername(principal.getName());

		List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
		if (cartItems.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		BigDecimal total = cartItems.stream()
				.map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		Order order = Order.builder().customerName(request.getCustomerName()).address(request.getAddress())
				.email(request.getEmail()).items(cartItems).total(total).status(OrderStatus.PENDING).build();

		orderRepository.save(order);

		cartItemRepository.deleteByUserId(user.getId());

		return ResponseEntity.ok(order);
	}
}