package com.ecom.service;

import com.ecom.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalAmount(Long id);
    Long initializeNewCart();

    Cart getCartByUserId(Long userId);
}
