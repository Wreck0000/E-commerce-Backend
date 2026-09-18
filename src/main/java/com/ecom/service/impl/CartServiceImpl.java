package com.ecom.service.impl;

import com.ecom.exception.ResourceNotFoundException;
import com.ecom.model.Cart;
import com.ecom.repository.CartItemRepository;
import com.ecom.repository.CartRepository;
import com.ecom.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final CartRepository cartRepository;
    private CartItemRepository cartitemRepository;
    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Cart not found"));
    }
    @Override
    public void clearCart(Long id) {
        Cart cart= getCart(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalAmount(Long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();
    }
}
