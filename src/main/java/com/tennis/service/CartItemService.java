package com.tennis.service;

import com.tennis.exception.ResourceNotFoundException;
import com.tennis.model.CartItem;
import com.tennis.model.Product;
import com.tennis.repository.CartItemRepository;
import com.tennis.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	// Add product to cart
	public void addProductToCart(Long productId, int quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		CartItem cartItem = new CartItem(product, quantity);
		cartItemRepository.save(cartItem);
	}

	// Get all cart items
	public List<CartItem> getCartItems() {
		return cartItemRepository.findAll();
	}

	// Remove product from cart
	public void removeProductFromCart(Long productId) {
		cartItemRepository.deleteByProductId(productId);
	}
}