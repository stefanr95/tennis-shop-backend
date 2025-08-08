package com.tennis.service;

import com.tennis.model.Product;
import com.tennis.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	public Product updateProduct(Long id, Product updatedProduct) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));

		product.setName(updatedProduct.getName());
		product.setDescription(updatedProduct.getDescription());
		product.setPrice(updatedProduct.getPrice());
		product.setImageUrl(updatedProduct.getImageUrl());
		product.setCategory(updatedProduct.getCategory());

		return productRepository.save(product);
	}

	public Page<Product> getAllProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

	public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
		return productRepository.searchByName(searchTerm, pageable);
	}
}