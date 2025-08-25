package com.tennis.repository;

import com.tennis.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByUserId(Long userId);

	Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

	void deleteByUserIdAndProductId(Long userId, Long productId);

	void deleteByUserId(Long userId);
}