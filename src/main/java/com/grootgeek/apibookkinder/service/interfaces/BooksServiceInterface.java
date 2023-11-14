package com.grootgeek.apibookkinder.service.interfaces;

import com.grootgeek.apibookkinder.entities.BookEntity;

import java.util.List;

public interface BooksServiceInterface {
    BookEntity saveNewBook(BookEntity newBook);

    List<BookEntity> findallByisbn(String isbn);

    BookEntity findallByisbnandseller(String isbn, String sellerUser);

    BookEntity updateBookApp(BookEntity updateBookApp);

    List<BookEntity> findall();

    Boolean deleteByIsbnAndSeller(String isbn, String sellerUser);

    BookEntity getInfoIsbn(String isbn);
}
