package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.User;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Created by z00382545 on 12/29/16.
 */

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CartItemService cartItemService;

    @RequestMapping("/addItem")
    public String addItem(
            @RequestParam("id") Long id,
            @RequestParam("qty") int qty,
            Model model, Principal principal) {

        Book book = bookService.findOne(id);
        User user = userService.findByUsername(principal.getName());

        CartItem cartItem = cartItemService.addBookToCartItem(book, user, qty);
        model.addAttribute("addBookSuccess", true);

        return "redirect:/bookDetail?id="+book.getId();
    }

}
