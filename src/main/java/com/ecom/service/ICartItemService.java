package com.ecom.service;

public interface ICartItemService {
    void addItemToCart(Long cartId,Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    void updateItemFromCart(Long cartId,Long productId,int quantity);

}
