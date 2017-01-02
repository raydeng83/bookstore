package com.bookstore.controller;

import com.bookstore.domain.Book;
import com.bookstore.domain.User;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * Created by z00382545 on 1/2/17.
 */

@Controller
public class SearchController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @RequestMapping("/searchByCategory")
    public String searchByCategory(
            @RequestParam("category") String category,
            Model model, Principal principal
    ) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        List<Book> bookList = bookService.findByCategory(category);

        model.addAttribute("bookList", bookList);

        model.addAttribute("active"+category, true);

        return "bookshelf";
    }
}
