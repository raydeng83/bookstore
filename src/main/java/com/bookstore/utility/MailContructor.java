package com.bookstore.utility;

import com.bookstore.domain.Order;
import com.bookstore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by z00382545 on 1/3/17.
 */
public class MailContructor {
    @Autowired
    private static Environment env;

    public static SimpleMailMessage constructResetTokenEmail(
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

    public static void constructOrderConfirmationEmail (User user, Order order) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver();
        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("user", user);
        context.setVariable("cartItemList", order.getCartItemList());
        String text = templateEngine.process("orderConfirmationEmailTemplate", context);

        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(user.getEmail());
                message.setText(text, true);
            }
        };

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.send(messagePreparator);
    }
}
