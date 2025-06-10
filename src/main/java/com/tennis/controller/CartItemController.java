package com.tennis.controller;

import com.tennis.dto.CartItemRequest;
import com.tennis.model.CartItem;
import com.tennis.service.CartItemService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

	private final CartItemService cartItemService;

	public CartItemController(CartItemService cartItemService) {
		this.cartItemService = cartItemService;
	}

	// Dodavanje proizvoda u korpu
	@PostMapping("/add")
	public void addProductToCart(@RequestBody CartItemRequest request) {
		cartItemService.addProductToCart(request.getProductId(), request.getQuantity());
	}

	// Dohvatanje svih proizvoda u korpi
	@GetMapping("/")
	public List<CartItem> getCartItems() {
		return cartItemService.getCartItems();
	}

	// Brisanje proizvoda iz korpe
	@DeleteMapping("/remove")
	public void removeProductFromCart(@RequestParam Long productId) {
		cartItemService.removeProductFromCart(productId);
	}
}