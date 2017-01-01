package com.bookstore.service.impl;

import com.bookstore.domain.ShippingAddress;
import com.bookstore.domain.UserShipping;
import com.bookstore.service.ShippingAddressService;
import org.springframework.stereotype.Service;

/**
 * Created by z00382545 on 12/30/16.
 */

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService{

    public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
        shippingAddress.setStreet1(userShipping.getStreet1());
        shippingAddress.setStreet2(userShipping.getStreet2());
        shippingAddress.setCity(userShipping.getCity());
        shippingAddress.setState(userShipping.getState());
        shippingAddress.setCity(userShipping.getCity());
        shippingAddress.setCountry(userShipping.getCountry());
        shippingAddress.setZipcode(userShipping.getZipcode());

        return shippingAddress;
    }
}
