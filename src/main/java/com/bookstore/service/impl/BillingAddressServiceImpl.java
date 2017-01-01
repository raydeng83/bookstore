package com.bookstore.service.impl;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.Payment;
import com.bookstore.domain.UserBilling;
import com.bookstore.service.BillingAddressService;
import org.springframework.stereotype.Service;

/**
 * Created by z00382545 on 12/30/16.
 */
@Service
public class BillingAddressServiceImpl implements BillingAddressService {

    public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress){
        billingAddress.setStreet1(userBilling.getStreet1());
        billingAddress.setStreet2(userBilling.getStreet2());
        billingAddress.setCity(userBilling.getCity());
        billingAddress.setState(userBilling.getState());
        billingAddress.setCountry(userBilling.getCountry());
        billingAddress.setZipcode(userBilling.getZipcode());

        return billingAddress;
    }
}
