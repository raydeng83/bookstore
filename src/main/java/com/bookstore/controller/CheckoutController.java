package com.bookstore.controller;

import com.bookstore.domain.*;
import com.bookstore.service.*;
import com.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.expression.Calendars;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by z00382545 on 12/27/16.
 */

@Controller
public class CheckoutController {

    private ShippingAddress shippingAddress = new ShippingAddress();
    private BillingAddress billingAddress = new BillingAddress();
    private Payment payment = new Payment();


    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private ShippingAddressService shippingAddressService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillingAddressService billingAddressService;

    @RequestMapping("/checkout")
    public String checkout(@RequestParam("id") Long cartId, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if(cartId != user.getShoppingCart().getId()) {
            return "badRequestPage";
        }

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

        for (CartItem cartItem : cartItemList) {
            if(cartItem.getBook().getInStockNumber()<cartItem.getQty()) {
                model.addAttribute("notEnoughStock", true);
                return "forward:/shoppingCart/cart";
            }
        }

        List<UserShipping> userShippingList = user.getUserShippingList();
        List<UserPayment> userPaymentList = user.getUserPaymentList();

        model.addAttribute("userShippingList", userShippingList);
        model.addAttribute("userPaymentList", userPaymentList);

        for (UserShipping userShipping : userShippingList) {
            if(userShipping.isDefaultShipping()) {
                shippingAddressService.setByUserShipping(userShipping,shippingAddress);
            }
        }

        for (UserPayment userPayment : userPaymentList) {
            if(userPayment.isDefaultPayment()) {
                paymentService.setByUserPayment(userPayment,payment);
                billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
            }
        }

        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", user.getShoppingCart());

        List<String> stateList = USConstants.listOfUSStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);


        model.addAttribute("classActiveShipping", true);

        return "checkout";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public String checkoutPost(
            @ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
            @ModelAttribute("billingAddress") BillingAddress billingAddress,
            @ModelAttribute("payment") Payment payment,
            @ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
            @ModelAttribute("shippingMethod") String shippingMethod,
            Principal principal,
            Model model
            ) {
        ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();

        if (billingSameAsShipping.equals("true")) {
            billingAddress.setStreet1(shippingAddress.getStreet1());
            billingAddress.setStreet2(shippingAddress.getStreet2());
            billingAddress.setCity(shippingAddress.getCity());
            billingAddress.setState(shippingAddress.getState());
            billingAddress.setCountry(shippingAddress.getCountry());
            billingAddress.setZipcode(shippingAddress.getZipcode());
        }

        orderService.createOrder(shoppingCart,shippingAddress,billingAddress,payment,shippingMethod);

        shoppingCartService.clearShoppingCart(shoppingCart);

        LocalDate today = LocalDate.now();
        LocalDate estimatedDeliveryDate;

        if (shippingMethod.equals("groundShipping")) {
            estimatedDeliveryDate = today.plusDays(5);
        } else {
            estimatedDeliveryDate = today.plusDays(3);
        }

        model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);

        return "orderSubmittedPage";
    }

    @RequestMapping("/setShippingAddress")
    public String setShippingAddress(
            @RequestParam("userShippingId") Long userShippingId,
            Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(userShippingId);

        if(userShipping.getUser().getId()!=user.getId()) {
            return "badRequestPage";
        } else {
            shippingAddressService.setByUserShipping(userShipping, shippingAddress);

            List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

            BillingAddress billingAddress = new BillingAddress();

            model.addAttribute("shippingAddress", shippingAddress);
            model.addAttribute("payment", payment);
            model.addAttribute("billingAddress", billingAddress);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("shoppingCart", user.getShoppingCart());

            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            List<UserShipping> userShippingList = user.getUserShippingList();
            List<UserPayment> userPaymentList = user.getUserPaymentList();

            model.addAttribute("userShippingList", userShippingList);
            model.addAttribute("userPaymentList", userPaymentList);
            model.addAttribute("shippingAddress", shippingAddress);

            model.addAttribute("classActiveShipping", true);

            return "checkout";
        }
    }

    @RequestMapping("/setPaymentMethod")
    public String setPaymentMethod(
            @RequestParam("userPaymentId") Long userPaymentId,
            Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(userPaymentId);
        UserBilling userBilling = userPayment.getUserBilling();

        if(userPayment.getUser().getId()!=user.getId()) {
            return "badRequestPage";
        } else {
            paymentService.setByUserPayment(userPayment, payment);

            List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

            billingAddressService.setByUserBilling(userBilling, billingAddress);

            model.addAttribute("shippingAddress", shippingAddress);
            model.addAttribute("payment", payment);
            model.addAttribute("billingAddress", billingAddress);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("shoppingCart", user.getShoppingCart());

            List<String> stateList = USConstants.listOfUSStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            List<UserShipping> userShippingList = user.getUserShippingList();
            List<UserPayment> userPaymentList = user.getUserPaymentList();

            model.addAttribute("userShippingList", userShippingList);
            model.addAttribute("userPaymentList", userPaymentList);
            model.addAttribute("shippingAddress", shippingAddress);

            model.addAttribute("classActivePayment", true);

            return "checkout";
        }
    }
}
