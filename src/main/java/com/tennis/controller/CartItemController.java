package com.tennis.controller;

import com.tennis.dto.CartItemRequest;
import com.tennis.model.CartItem;
import com.tennis.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartItemController {

	private final CartItemService cartItemService;

	public CartItemController(CartItemService cartItemService) {
		this.cartItemService = cartItemService;
	}

	@PostMapping
	public ResponseEntity<Void> addProductToCart(@RequestBody CartItemRequest request) {
		cartItemService.addProductToCart(request.getProductId(), request.getQuantity());
		return ResponseEntity.status(201).build();
	}

	@GetMapping
	public ResponseEntity<List<CartItem>> getCartItems() {
		List<CartItem> items = cartItemService.getCartItems();
		return ResponseEntity.ok(items);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> removeProductFromCart(@PathVariable Long productId) {
		cartItemService.removeProductFromCart(productId);
		return ResponseEntity.noContent().build();
	}
}