package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by z00382545 on 12/27/16.
 */

@Controller
public class CheckoutController {

    @RequestMapping("/shoppingCart")
    public String shoppingCart() {
        return "shoppingCart";
    }

    @RequestMapping("/checkout")
    public String checkout() {
        return "checkout";
    }
}
