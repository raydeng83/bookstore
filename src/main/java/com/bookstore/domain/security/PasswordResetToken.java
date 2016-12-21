package com.bookstore.domain.security;

import com.bookstore.domain.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by z00382545 on 12/20/16.
 */
@Entity
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;
}
