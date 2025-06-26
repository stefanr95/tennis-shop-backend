package com.tennis.model;

import jakarta.persistence.*;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private int quantity;

	// Constructors
	public CartItem() {
	}

	public CartItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}