package com.tennis.service;

import com.tennis.exception.ResourceNotFoundException;
import com.tennis.model.CartItem;
import com.tennis.model.Product;
import com.tennis.model.User;
import com.tennis.repository.CartItemRepository;
import com.tennis.repository.ProductRepository;
import com.tennis.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartItemServiceTest {

	@Mock
	private CartItemRepository cartItemRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private CartItemService cartItemService;

	@Mock
	private SecurityContext securityContext;

	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setId(1L);

		UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
		when(userDetails.getUser()).thenReturn(user);

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userDetails);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	void addProductToCart_shouldAddNewCartItem() {
		Product product = new Product();
		product.setId(1L);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

		cartItemService.addProductToCart(1L, 2);

		verify(cartItemRepository, times(1)).save(any(CartItem.class));
	}

	@Test
	void addProductToCart_shouldIncreaseQuantityIfExists() {
		Product product = new Product();
		product.setId(1L);

		CartItem existingItem = new CartItem(product, 1, user);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(existingItem));

		cartItemService.addProductToCart(1L, 3);

		assertEquals(4, existingItem.getQuantity());
		verify(cartItemRepository, times(1)).save(existingItem);
	}

	@Test
	void getCartItems_shouldReturnItems() {
		CartItem item1 = new CartItem();
		CartItem item2 = new CartItem();

		when(cartItemRepository.findByUserId(1L)).thenReturn(Arrays.asList(item1, item2));

		List<CartItem> items = cartItemService.getCartItems();

		assertEquals(2, items.size());
	}

	@Test
	void removeProductFromCart_shouldCallDelete() {
		cartItemService.removeProductFromCart(1L);

		verify(cartItemRepository, times(1)).deleteByUserIdAndProductId(1L, 1L);
	}

	@Test
	void updateQuantity_shouldUpdateExistingItem() {
		Product product = new Product();
		product.setId(1L);
		CartItem cartItem = new CartItem(product, 2, user);

		when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));

		cartItemService.updateQuantity(1L, 5);

		assertEquals(5, cartItem.getQuantity());
		verify(cartItemRepository, times(1)).save(cartItem);
	}

	@Test
	void updateQuantity_shouldDeleteIfQuantityLessThanOne() {
		Product product = new Product();
		product.setId(1L);
		CartItem cartItem = new CartItem(product, 2, user);

		when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(cartItem));

		cartItemService.updateQuantity(1L, 0);

		verify(cartItemRepository, times(1)).delete(cartItem);
	}

	@Test
	void updateQuantity_shouldThrowIfItemNotFound() {
		when(cartItemRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> cartItemService.updateQuantity(1L, 2));
	}
}