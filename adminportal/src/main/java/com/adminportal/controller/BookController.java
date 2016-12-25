package com.adminportal.controller;

import com.adminportal.domain.Book;
import com.adminportal.service.BookService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by z00382545 on 12/25/16.
 */

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/add")
    public String addBook(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "addBook";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBookPost (@ModelAttribute("book") Book book) {
        bookService.save(book);

        return "addBook";
    }
}
