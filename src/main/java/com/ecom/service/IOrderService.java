package com.ecom.service;

import com.ecom.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);


}
