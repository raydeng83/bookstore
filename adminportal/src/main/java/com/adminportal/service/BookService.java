package com.adminportal.service;

import com.adminportal.domain.Book;

import java.util.List;

/**
 * Created by z00382545 on 12/25/16.
 */
public interface BookService {
    Book save(Book book);

    Book findOne(Long id);

    List<Book> findAll();

    void removeOne(Long id);
}
