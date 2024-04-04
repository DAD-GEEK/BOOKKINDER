package com.grootgeek.apibookkinder.controller;

import com.grootgeek.apibookkinder.dto.BookDto;
import com.grootgeek.apibookkinder.dto.ResponseApiDto;
import com.grootgeek.apibookkinder.entities.BookEntity;
import com.grootgeek.apibookkinder.service.BooksService;
import com.grootgeek.apibookkinder.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping
public class BookController {

    private final BooksService booksService;
    private final Logger utilLogs;

    @Value("${book.seller}")
    String sellerBook;

    @Value("${book.isbn}")
    String isbnBook;


    @Autowired
    public BookController(BooksService booksService, Logger utilLogs) {
        this.booksService = booksService;
        this.utilLogs = utilLogs;

    }

    @PostMapping("/book/new")
    public ResponseApiDto<Object> newBook(@RequestBody BookDto newBookReq, HttpServletRequest request) {
        String logNewBook;
        ResponseApiDto<Object> responseNewBook = new ResponseApiDto<>();
        utilLogs.logApi(0, "Create new Book ISBN: " + newBookReq.getIsbn() + sellerBook + newBookReq.getSellerUser(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final BookEntity validateBook = booksService.findallByisbnandseller(newBookReq.getIsbn(), newBookReq.getSellerUser());
        if (validateBook != null) {
            responseNewBook.responseError(false, "Book-01", "Book already exists");
            logNewBook = responseNewBook.getCode() + isbnBook + newBookReq.getIsbn() + sellerBook + newBookReq.getSellerUser() + " " + responseNewBook.getMessage();
            utilLogs.logApi(1, logNewBook, BookController.class.getSimpleName(), request.getRemoteAddr());
        } else {
            newBookReq.setId(newBookReq.getIsbn() + newBookReq.getSellerUser());
            final BookEntity createBook = booksService.saveNewBook(newBookReq);
            if (createBook != null) {
                responseNewBook.responseSuccess(true, "Book-01", "Book created successfully", createBook);
                logNewBook = responseNewBook.getMessage() + ": " + responseNewBook.getData() + " ";
            } else {
                responseNewBook.responseError(false, "ErrorBook-01", "Error creating Book");
                logNewBook = responseNewBook.getCode() + isbnBook + newBookReq.getIsbn() + sellerBook + newBookReq.getSellerUser();
                utilLogs.logApiError(logNewBook);
            }
            utilLogs.logApi(1, logNewBook, BookController.class.getSimpleName(), request.getRemoteAddr());
        }
        return responseNewBook;
    }

    @PostMapping("/book/read/all")
    public ResponseApiDto<Object> readAllBooks(HttpServletRequest request) {
        String logReadAllBooks;
        ResponseApiDto<Object> responseReadAll = new ResponseApiDto<>();
        utilLogs.logApi(0, "Read all Books APP ", BookController.class.getSimpleName(), request.getRemoteAddr());
        final List<BookEntity> readAllBooks = booksService.findall();
        if (readAllBooks != null) {
            responseReadAll.responseSuccess(true, "Book-02", "Books Read successfully", readAllBooks);
            logReadAllBooks = responseReadAll.getMessage() + ": Users found = " + readAllBooks.size() + " ";
        } else {
            responseReadAll.responseError(false, "ErrorBook-02", "Error Read Books");
            logReadAllBooks = responseReadAll.getCode() + " " + responseReadAll.getMessage();
            utilLogs.logApiError(logReadAllBooks);
        }
        utilLogs.logApi(1, logReadAllBooks, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseReadAll;
    }

    @PostMapping("/book/update")
    public ResponseApiDto<Object> updateBook(@RequestBody BookDto updateBookReq, HttpServletRequest request) {
        String logUpdateBook;
        ResponseApiDto<Object> responseUpdateBook = new ResponseApiDto<>();
        utilLogs.logApi(0, "Update Book APP ISBN: " + updateBookReq.getIsbn() + sellerBook + updateBookReq.getSellerUser(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final BookEntity updateBook = booksService.updateBookApp(updateBookReq);
        if (updateBook != null) {
            responseUpdateBook.responseSuccess(true, "Book-03", "Book update successfully", updateBook);
            logUpdateBook = responseUpdateBook.getMessage() + ": " + responseUpdateBook.getData() + " ";
        } else {
            responseUpdateBook.responseError(false, "ErrorBook-03", "Error updating Book");
            logUpdateBook = responseUpdateBook.getCode() + isbnBook + updateBookReq.getIsbn() + sellerBook + updateBookReq.getSellerUser();
            utilLogs.logApiError(logUpdateBook);
        }
        utilLogs.logApi(1, logUpdateBook, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseUpdateBook;
    }

    @PostMapping("/book/delete")
    public ResponseApiDto<Object> deleteBook(@RequestBody BookDto deleteBookReq, HttpServletRequest request) {
        String logDeleteBook;
        ResponseApiDto<Object> responseDeleteBook = new ResponseApiDto<>();
        utilLogs.logApi(0, "Delete Book APP " + isbnBook + deleteBookReq.getIsbn() + sellerBook + deleteBookReq.getSellerUser(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final boolean deleteBook = booksService.deletebyIsbnandSeller(deleteBookReq.getIsbn(), deleteBookReq.getSellerUser());
        if (deleteBook) {
            responseDeleteBook.responseSuccess(true, "Book-04", "Book delete successfully", null);
            logDeleteBook = responseDeleteBook.getMessage() + isbnBook + deleteBookReq.getIsbn() + sellerBook + deleteBookReq.getSellerUser();
        } else {
            responseDeleteBook.responseError(false, "ErrorBook-04", "Error delete Book");
            logDeleteBook = responseDeleteBook.getCode() + isbnBook + deleteBookReq.getIsbn() + sellerBook + deleteBookReq.getSellerUser();
            utilLogs.logApiError(logDeleteBook);
        }
        utilLogs.logApi(1, logDeleteBook, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseDeleteBook;
    }

    @PostMapping("/book/search/isbn")
    public ResponseApiDto<Object> searchIsbn(@RequestBody BookDto searchReq, HttpServletRequest request) {
        String logSearchIsbn;
        ResponseApiDto<Object> responseSearchIsbn = new ResponseApiDto<>();
        utilLogs.logApi(0, "Search Info BOOK Google isbn " + searchReq.getIsbn(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final BookEntity bookmarked = booksService.getInfoIsbn(searchReq.getIsbn());
        if (Objects.equals(bookmarked.getId(), "") || bookmarked.getId() == null) {
            responseSearchIsbn.responseSuccess(true, "Book-05", "Book searched successfully", bookmarked);
            logSearchIsbn = responseSearchIsbn.getMessage() + ": Book found = " + bookmarked.getName() + " author:" + bookmarked.getAuthor();
        } else if (bookmarked.getId().equals("0")) {
            responseSearchIsbn.responseSuccess(true, "Book-05", "Can't no found Info Google BOOKS " + isbnBook + searchReq.getIsbn(), bookmarked);
            logSearchIsbn = responseSearchIsbn.getMessage();
        } else {
            responseSearchIsbn.responseError(false, "ErrorBook-05", "Error search Book in Google BOOKS");
            logSearchIsbn = responseSearchIsbn.getCode() + " " + responseSearchIsbn.getMessage();
            utilLogs.logApiError(logSearchIsbn);
        }
        utilLogs.logApi(1, logSearchIsbn, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseSearchIsbn;
    }

}
