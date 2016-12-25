package com.adminportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by z00382545 on 12/24/16.
 */

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
