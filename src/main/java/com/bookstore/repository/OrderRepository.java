package com.bookstore.repository;

import com.bookstore.domain.Order;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by z00382545 on 12/30/16.
 */
public interface OrderRepository extends CrudRepository<Order, Long> {
}
