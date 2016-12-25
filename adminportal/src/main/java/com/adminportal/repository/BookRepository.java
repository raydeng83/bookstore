package com.adminportal.repository;

import com.adminportal.domain.Book;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by z00382545 on 12/25/16.
 */
public interface BookRepository extends CrudRepository<Book, Long> {
}
