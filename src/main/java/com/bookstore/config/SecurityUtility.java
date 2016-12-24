package com.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Created by z00382545 on 12/23/16.
 */

@Component
public class SecurityUtility {

    private static final String SALT = "salt"; // Salt should be protected carefully

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }
}
