package com.tennis.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String customerName;
	private String address;
	private String email;

	@Builder.Default
	private LocalDateTime orderDate = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private OrderStatus status = OrderStatus.PENDING;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<CartItem> items;

	private BigDecimal total;
}