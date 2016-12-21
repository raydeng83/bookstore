package com.bookstore.controller;

import com.bookstore.domain.GenericResponse;
import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserService;
import com.bookstore.service.impl.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by lede on 12/18/16.
 */

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private UserSecurityService userSecurityService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/myAccount")
    public String myAccount() {
        return "myAccount";
    }

    @RequestMapping("/myProfile")
    public String myProfile() {
        return "myProfile";
    }

//    @RequestMapping(value = "/resetEmail", method = RequestMethod.POST)
//    public String resetEmail(@ModelAttribute("email") String email, Model model) {
//        model.addAttribute("email", email);
//
//        return "emailSentPage";
//    }

//    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
//    @ResponseBody
//    public GenericResponse resetPassword(HttpServletRequest request, @ModelAttribute("email") String userEmail) throws Exception {
//        User user = userService.findByEmail(userEmail);
//
//        if (user == null) {
//            throw new Exception("User not found");
//        }
//
//        String token = UUID.randomUUID().toString();
//        userService.createPasswordResetTokenForUser(user, token);
//
//        String appUrl =
//                "http://" + request.getServerName() +
//                        ":" + request.getServerPort() +
//                        request.getContextPath();
//
//        SimpleMailMessage email =
//                constructResetTokenEmail(appUrl, request.getLocale(), token, user);
//
//        mailSender.send(email);
//
//        return new GenericResponse(
//                messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
//    }
//

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUser(HttpServletRequest request,
                                   @ModelAttribute("email") String userEmail,
                                   @ModelAttribute("username") String username) throws Exception {
        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);
        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();

        SimpleMailMessage email =
                constructResetTokenEmail(appUrl, request.getLocale(), token, user);

        mailSender.send(email);

        return "myAccount";

    }

    @RequestMapping(value = "/user/addNewUser", method = RequestMethod.GET)
    public String addNewUser(
            Locale locale, Model model, @RequestParam("token") String token) {

        PasswordResetToken passToken = userService.getPasswordResetToken(token);
        User user = passToken.getUser();
        if (passToken == null ) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/myAccount";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "Token has expired.");
            return "redirect:/myAccount";
        }

//        Authentication auth = new UsernamePasswordAuthenticationToken(
//                user, "test", userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);

        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "myProfile";
    }

    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "/user/addNewUser?"+"token=" + token;
        String message = "Please click on this link to verify your email and edit your personal info. ";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Le's Bookstore - New User");
        email.setText(message + ": " + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
