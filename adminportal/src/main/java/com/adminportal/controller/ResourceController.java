package com.adminportal.controller;

import com.adminportal.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by z00382545 on 1/3/17.
 */
@RestController
public class ResourceController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/book/removeList", method = RequestMethod.POST)
    public String removeList(
            @RequestBody ArrayList<String> bookIdList, Model model
    ){
        for (String id : bookIdList) {
            String bookId = id.substring(8);
            bookService.removeOne(Long.parseLong(bookId));
        }

        return "delete success";
    }
}
