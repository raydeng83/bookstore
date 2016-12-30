package com.bookstore.controller;

import com.bookstore.domain.CartItem;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * Created by z00382545 on 12/27/16.
 */

@Controller
public class CheckoutController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @RequestMapping("/checkout")
    public String checkout(@RequestParam("id") Long cartId, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if(cartId != user.getShoppingCart().getId()) {
            return "badRequestPage";
        }

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

        for (CartItem cartItem : cartItemList) {
            if(cartItem.getBook().getInStockNumber()<cartItem.getQty()) {
                model.addAttribute("notEnoughStock", true);
                return "forward:/shoppingCart/cart";
            }
        }

        return "checkout";
    }
}
