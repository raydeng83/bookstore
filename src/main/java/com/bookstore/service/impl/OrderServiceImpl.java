package com.bookstore.service.impl;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.Order;
import com.bookstore.domain.Payment;
import com.bookstore.domain.ShippingAddress;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by z00382545 on 12/30/16.
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

//    public Order createOrder(
//            ShippingAddress shippingAddress,
//            BillingAddress billingAddress,
//            Payment payment,
//            String shippingMethod
//            ) {
//        Order order = new Order();
//
//    }
}
