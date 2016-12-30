package com.adminportal.repository;

import com.adminportal.domain.ShoppingCart;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by z00382545 on 12/29/16.
 */
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
}
