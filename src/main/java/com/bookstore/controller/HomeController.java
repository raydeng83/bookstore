package com.bookstore.controller;

import com.bookstore.config.SecurityConfig;
import com.bookstore.config.SecurityUtility;
import com.bookstore.domain.Book;
import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.BookService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.security.Principal;
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

    @Autowired
    private BookService bookService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/bookshelf")
    public String bookshelf(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        List<Book> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);

        return "bookshelf";
    }

    @RequestMapping("/bookDetail")
    public String bookDetail(@PathParam("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        Book book = bookService.findOne(id);

        model.addAttribute("book", book);
        List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty","1");
        return "bookDetail";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", "true");
        return "myAccount";
    }


    @RequestMapping("/forgetPassword")
    public String forgetPassword(@ModelAttribute("email") String email,
                                 HttpServletRequest request,
                                 Model model) {
        model.addAttribute("classActiveForgetPassword", "true");

        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("emailNotExists", true);
            return "myAccount";
        }

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();

        SimpleMailMessage newEmail =
                constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        mailSender.send(newEmail);

        model.addAttribute("emailSent", true);

        return "myAccount";
    }

    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        return "myProfile";
    }

    @RequestMapping("/badRequest")
    public String badRequest() {
        return "badRequestPage";
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUser(HttpServletRequest request,
                          @ModelAttribute("email") String userEmail,
                          @ModelAttribute("username") String username,
                          Model model
    ) throws Exception {
        model.addAttribute("classActiveNewAccount", "true");
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);


//        check username exists
        if (userService.findByUsername(username) != null) {
            model.addAttribute("usernameExists", true);

            return "myAccount";
        }

//        check email address exists
        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);

            return "myAccount";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

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
                constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        mailSender.send(email);

        model.addAttribute("emailSent", "true");

        return "myAccount";

    }

    @RequestMapping(value = "/user/addNewUser", method = RequestMethod.GET)
    public String addNewUser(
            Locale locale, Model model,
            @RequestParam("token") String token) {

        PasswordResetToken passToken = userService.getPasswordResetToken(token);
        if (passToken == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/badRequest";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "Token has expired.");
            return "redirect:/badRequest";
        }

//        Authentication auth = new UsernamePasswordAuthenticationToken(
//                user, "test", userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = passToken.getUser();

        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);

        return "myProfile";
    }

    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user, String password) {
        String url = contextPath + "/user/addNewUser?token=" + token;
        String message = "\nPlease click on this link to verify your email and edit your personal info. Your password is:\n " + password;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Le's Bookstore - New User");
        email.setText(url + message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public String profileInfo(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) throws Exception {

        User currentUser = userService.findById(user.getId());

        if (currentUser == null) {
            throw new Exception("User not found");
        }

//      check email address exists
        if (userService.findByEmail(user.getEmail()) != null) {
            if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
                model.addAttribute("emailExists", "true");
                return "myProfile";
            }
        }

//        check username exists
        if (userService.findByUsername(user.getUsername()) != null) {
            if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
                model.addAttribute("usernameExists", "true");
                return "myProfile";
            }
        }

        SecurityConfig securityConfig = new SecurityConfig();

//      update password
        if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
            BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
            if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);

                return "myProfile";
            }
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        userService.save(currentUser);

        model.addAttribute("updateSuccess", "true");
        model.addAttribute("user", currentUser);

        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "myProfile";
    }
}
