package com.tennis.service;

import com.tennis.model.Product;
import com.tennis.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void addProduct_shouldSaveProduct() {
		Product product = Product.builder().id(1L).name("Tennis Racket").description("Pro racket")
				.price(BigDecimal.valueOf(199.99)).imageUrl("image.png").category("Rackets").build();

		when(productRepository.save(product)).thenReturn(product);

		Product saved = productService.addProduct(product);

		assertNotNull(saved);
		assertEquals("Tennis Racket", saved.getName());
		assertEquals(BigDecimal.valueOf(199.99), saved.getPrice());

		verify(productRepository, times(1)).save(product);
	}

	@Test
	void updateProduct_shouldUpdateExistingProduct() {
		Product existing = Product.builder().id(1L).name("Old Racket").description("Old desc")
				.price(BigDecimal.valueOf(100.00)).imageUrl("old.png").category("OldCat").build();

		Product updated = Product.builder().name("New Racket").description("New desc").price(BigDecimal.valueOf(250.00))
				.imageUrl("new.png").category("NewCat").build();

		when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

		Product result = productService.updateProduct(1L, updated);

		assertEquals("New Racket", result.getName());
		assertEquals(BigDecimal.valueOf(250.00), result.getPrice());
		assertEquals("new.png", result.getImageUrl());
		assertEquals("NewCat", result.getCategory());

		verify(productRepository, times(1)).save(existing);
	}

	@Test
	void updateProduct_shouldThrowIfNotFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> productService.updateProduct(1L, new Product()));

		assertEquals("Product not found.", ex.getMessage());
	}

	@Test
	void getAllProducts_shouldReturnPagedProducts() {
		Pageable pageable = PageRequest.of(0, 10);
		Product product = Product.builder().id(1L).name("Ball").price(BigDecimal.valueOf(9.99)).build();

		Page<Product> page = new PageImpl<>(List.of(product));

		when(productRepository.findAll(pageable)).thenReturn(page);

		Page<Product> result = productService.getAllProducts(pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals("Ball", result.getContent().get(0).getName());
	}

	@Test
	void deleteProduct_shouldCallRepository() {
		productService.deleteProduct(1L);

		verify(productRepository, times(1)).deleteById(1L);
	}

	@Test
	void searchProducts_shouldReturnResults() {
		Pageable pageable = PageRequest.of(0, 10);
		Product product = Product.builder().id(1L).name("Shoes").price(BigDecimal.valueOf(59.99)).build();

		Page<Product> page = new PageImpl<>(List.of(product));

		when(productRepository.findByNameContainingIgnoreCase("Shoes", pageable)).thenReturn(page);

		Page<Product> result = productService.searchProducts("Shoes", pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals("Shoes", result.getContent().get(0).getName());
	}
}