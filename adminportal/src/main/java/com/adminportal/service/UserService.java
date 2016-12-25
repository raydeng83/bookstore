package com.adminportal.service;

import com.adminportal.domain.User;
import com.adminportal.domain.security.UserRole;

import java.util.Set;

/**
 * Created by z00382545 on 12/25/16.
 */
public interface UserService {
    User createUser (User user, Set<UserRole> userRoles);

    User save (User user);

    User findById (Long id);

    User findByUsername (String username);

    User findByEmail(String email);
}
