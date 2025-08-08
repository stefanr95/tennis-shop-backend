package com.tennis.controller;

import com.tennis.model.Product;
import com.tennis.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(productService.getAllProducts(pageable));
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		return ResponseEntity.ok(productService.addProduct(product));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
		return ResponseEntity.ok(productService.updateProduct(id, updatedProduct));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<Page<Product>> getProducts(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Product> products;

		if (search == null || search.trim().isEmpty()) {
			products = productService.getAllProducts(pageable);
		} else {
			products = productService.searchProducts(search.trim(), pageable);
		}

		return ResponseEntity.ok(products);
	}
}