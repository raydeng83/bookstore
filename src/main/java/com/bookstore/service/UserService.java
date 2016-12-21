package com.bookstore.service;

import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;

import java.util.Set;

/**
 * Created by z00382545 on 12/21/16.
 */
public interface UserService {
    User createUser (User user, Set<UserRole> userRoles);

    User findByEmail(String email);

    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);
}