package com.bookstore.service.impl;

import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by z00382545 on 12/30/16.
 */

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{

    @Autowired
    private CartItemService cartItemService;

    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {

        BigDecimal cartTotal = new BigDecimal(0);

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getBook().getInStockNumber()>0) {
                cartItemService.updateCartItem(cartItem);
                cartTotal = cartTotal.add(cartItem.getSubtotal());
            }
        }

        shoppingCart.setGrandTotal(cartTotal);

        return shoppingCart;
    }
}
