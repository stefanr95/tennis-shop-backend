package com.tennis.service;

import com.tennis.exception.ResourceNotFoundException;
import com.tennis.model.Order;
import com.tennis.model.OrderStatus;
import com.tennis.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createOrder_shouldSaveOrder() {
		Order order = new Order();
		order.setId(1L);

		when(orderRepository.save(order)).thenReturn(order);

		Order saved = orderService.createOrder(order);

		assertNotNull(saved);
		assertEquals(1L, saved.getId());
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	void getAllOrders_shouldReturnOrders() {
		Order o1 = new Order();
		o1.setId(1L);
		Order o2 = new Order();
		o2.setId(2L);
		List<Order> orders = Arrays.asList(o1, o2);

		when(orderRepository.findAll()).thenReturn(orders);

		List<Order> result = orderService.getAllOrders();

		assertEquals(2, result.size());
		verify(orderRepository, times(1)).findAll();
	}

	@Test
	void getOrderById_shouldReturnOrder() {
		Order order = new Order();
		order.setId(1L);
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		Order found = orderService.getOrderById(1L);

		assertNotNull(found);
		assertEquals(1L, found.getId());
	}

	@Test
	void getOrderById_shouldThrowIfNotFound() {
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
	}

	@Test
	void deleteOrder_shouldDeleteIfExists() {
		when(orderRepository.existsById(1L)).thenReturn(true);

		orderService.deleteOrder(1L);

		verify(orderRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteOrder_shouldThrowIfNotExists() {
		when(orderRepository.existsById(1L)).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(1L));
		verify(orderRepository, never()).deleteById(1L);
	}

	@Test
	void updateOrderStatus_shouldUpdateStatus() {
		Order order = new Order();
		order.setId(1L);
		order.setStatus(OrderStatus.PENDING);

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		when(orderRepository.save(order)).thenReturn(order);

		Order updated = orderService.updateOrderStatus(1L, OrderStatus.SHIPPED);

		assertEquals(OrderStatus.SHIPPED, updated.getStatus());
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	void updateOrderStatus_shouldThrowIfAlreadyInStatus() {
		Order order = new Order();
		order.setId(1L);
		order.setStatus(OrderStatus.SHIPPED);

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		assertThrows(IllegalStateException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.SHIPPED));
	}

	@Test
	void getOrderStatus_shouldReturnStatus() {
		Order order = new Order();
		order.setId(1L);
		order.setStatus(OrderStatus.PENDING);

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		OrderStatus status = orderService.getOrderStatus(1L);

		assertEquals(OrderStatus.PENDING, status);
	}
}