package com.tennis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tennis.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}