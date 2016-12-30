package com.bookstore.service;

import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;

import java.util.List;

/**
 * Created by z00382545 on 12/29/16.
 */
public interface CartItemService {

    CartItem addBookToCartItem(Book book, User user, int qty );

    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

    CartItem updateCartItem(CartItem cartItem);

    void removeCartItem (CartItem cartItem);

    CartItem findById(Long id);
}