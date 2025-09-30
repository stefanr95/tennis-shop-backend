package com.tennis.service;

import com.tennis.exception.ResourceNotFoundException;
import com.tennis.model.CartItem;
import com.tennis.model.Product;
import com.tennis.model.User;
import com.tennis.repository.CartItemRepository;
import com.tennis.repository.ProductRepository;
import com.tennis.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	private User getCurrentUser() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return userDetails.getUser();
	}

	public void addProductToCart(Long productId, int quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		User user = getCurrentUser();

		CartItem cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), productId).map(item -> {
			item.setQuantity(item.getQuantity() + quantity);
			return item;
		}).orElse(new CartItem(product, quantity, user));

		cartItemRepository.save(cartItem);
	}

	public List<CartItem> getCartItems() {
		User user = getCurrentUser();
		return cartItemRepository.findByUserId(user.getId());
	}

	@Transactional
	public void removeProductFromCart(Long productId) {
		User user = getCurrentUser();
		cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
	}

	@Transactional
	public void updateQuantity(Long productId, int quantity) {
		User user = getCurrentUser();

		CartItem cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), productId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found for product id: " + productId));

		if (quantity < 1) {
			cartItemRepository.delete(cartItem);
		} else {
			cartItem.setQuantity(quantity);
			cartItemRepository.save(cartItem);
		}
	}
}