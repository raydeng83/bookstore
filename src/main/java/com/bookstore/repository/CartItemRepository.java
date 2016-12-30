package com.bookstore.repository;

import com.bookstore.domain.CartItem;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by z00382545 on 12/29/16.
 */
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
}
