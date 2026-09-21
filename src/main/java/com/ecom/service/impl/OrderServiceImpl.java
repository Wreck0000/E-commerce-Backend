package com.ecom.service.impl;
import com.ecom.exception.ResourceNotFoundException;
import com.ecom.model.Cart;
import com.ecom.model.Order;
import com.ecom.model.OrderItem;
import com.ecom.repository.CartRepository;
import com.ecom.repository.OrderRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ICartService;
import com.ecom.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final ICartService cartService;
    private final ProductRepository productRepository;
    @Override
    public Order placeOrder(Long userId) {
        Cart cart=cartService.getCart(userId);
        if(cart==null){
        }
    }
    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found"));
    }
    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem->{

        });
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemsList) {
        return orderItemsList.stream()
                .map(item->item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}