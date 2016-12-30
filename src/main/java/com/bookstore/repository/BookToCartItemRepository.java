package com.bookstore.repository;

import com.bookstore.domain.BookToCartItem;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by z00382545 on 12/29/16.
 */
public interface BookToCartItemRepository extends CrudRepository<BookToCartItem,Long>{
}
