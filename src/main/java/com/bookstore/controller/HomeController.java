package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by lede on 12/18/16.
 */

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/myAccount")
    public String myAccount() {
        return "myAccount";
    }

    @RequestMapping(value = "/resetEmail", method = RequestMethod.POST)
    public String resetEmail(@ModelAttribute("email") String email, Model model) {
        model.addAttribute("email", email);

        return "emailSentPage";
    }
}
