package com.ecom.service.impl;

import com.ecom.exception.ResourceNotFoundException;
import com.ecom.model.Cart;
import com.ecom.model.CartItem;
import com.ecom.model.Product;
import com.ecom.repository.CartItemRepository;
import com.ecom.repository.CartRepository;
import com.ecom.service.ICartItemService;
import com.ecom.service.ICartService;
import com.ecom.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Fetch Cart and Product
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        // 2. Check if product already exists in cart
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        // 3. Create new or update existing item
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        // 4. Calculate total for line item and update cart
        cartItem.setTotalPrice();
        cart.addItem(cartItem);

        // 5. Save changes
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateItemFromCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                }, () -> {
                    throw new ResourceNotFoundException("Product not found in cart");
                });
        BigDecimal totalAmount = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }
}
