package com.adminportal.service.impl;

import com.adminportal.domain.ShoppingCart;
import com.adminportal.repository.ShoppingCartRepository;
import com.adminportal.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by z00382545 on 12/29/16.
 */

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }
}
