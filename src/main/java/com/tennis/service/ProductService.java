package com.tennis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.tennis.model.Product;
import com.tennis.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	// Konstruktor za ProductRepository
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// Dodavanje novog proizvoda
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	// Izmena postojećeg proizvoda
	public Product updateProduct(Long id, Product updatedProduct) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));

		product.setName(updatedProduct.getName());
		product.setDescription(updatedProduct.getDescription());
		product.setPrice(updatedProduct.getPrice());
		product.setImageUrl(updatedProduct.getImageUrl());
		product.setCategory(updatedProduct.getCategory());

		return productRepository.save(product);
	}

	// Dohvatanje svih proizvoda
	public Page<Product> getAllProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	// Brisanje proizvoda
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}