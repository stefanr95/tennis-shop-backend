package com.tennis.service;

import com.tennis.model.CartItem;
import com.tennis.model.Product;
import com.tennis.repository.CartItemRepository;
import com.tennis.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartItemService {

	private CartItemRepository cartItemRepository;
	private ProductRepository productRepository;

	public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
	}

	// Dodavanje proizvoda u korpu
	public void addProductToCart(Long productId, int quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Proizvod nije pronađen"));

		CartItem cartItem = new CartItem(product, quantity);
		cartItemRepository.save(cartItem);
	}

	// Dohvatanje proizvoda iz korpe
	public List<CartItem> getCartItems() {
		return cartItemRepository.findAll();
	}

	// Brisanje proizvoda iz korpe
	public void removeProductFromCart(Long productId) {
		cartItemRepository.deleteByProductId(productId);
	}
}