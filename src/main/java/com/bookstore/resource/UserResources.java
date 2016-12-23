package com.bookstore.resource;

import com.bookstore.domain.User;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by z00382545 on 12/22/16.
 */

@RestController
public class UserResources {

    @Autowired
    private UserService userService;


}
