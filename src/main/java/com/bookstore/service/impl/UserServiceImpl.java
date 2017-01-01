package com.bookstore.service.impl;

import com.bookstore.config.SecurityUtility;
import com.bookstore.domain.*;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.*;
import com.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by z00382545 on 12/21/16.
 */

@Service
public class UserServiceImpl implements UserService{

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserShippingRepository userShippingRepository;

    @Autowired
    private UserBillingRepository userBillingRepository;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Transactional
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
        } else {


            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            user.setShoppingCart(shoppingCart);

            UserShipping userShipping = new UserShipping();
            UserBilling userBilling = new UserBilling();
            UserPayment userPayment = new UserPayment();

            userBilling.setUser(user);
            userPayment.setUser(user);
            userShipping.setUser(user);
            user.setUserBillingList(new ArrayList<UserBilling>(Arrays.asList(userBilling)));
            user.setUserShippingList(new ArrayList<UserShipping>(Arrays.asList(userShipping)));
            user.setUserPaymentList(new ArrayList<UserPayment>(Arrays.asList(userPayment)));

            localUser = userRepository.save(user);
        }

        return localUser;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public void updateUserPaymentInfo(UserShipping userShipping, UserBilling userBilling, UserPayment userPayment, User user) {

        save(user);
        userBillingRepository.save(userBilling);
        userShippingRepository.save(userShipping);
        userPaymentRepository.save(userPayment);
    }
}
