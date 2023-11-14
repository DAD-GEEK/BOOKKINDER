package com.grootgeek.apibookkinder.controller;

import com.grootgeek.apibookkinder.dto.RespuestaApiDto;
import com.grootgeek.apibookkinder.entities.BookEntity;
import com.grootgeek.apibookkinder.service.BooksService;
import com.grootgeek.apibookkinder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Utils utilLogs;

    @Autowired
    public BookController(BooksService booksService, Utils utilLogs) {
        this.booksService = booksService;
        this.utilLogs = utilLogs;
    }

    @PostMapping("/book/new")
    public RespuestaApiDto<Object> newBook(@RequestBody BookEntity newBookReq, HttpServletRequest request) {
        String logbook;
        RespuestaApiDto<Object> responseNew = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Create new Book ISBN: " + newBookReq.getIsbn() + " sellerBook: " + newBookReq.getSellerUser(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final BookEntity validateBook = booksService.findallByisbnandseller(newBookReq.getIsbn(), newBookReq.getSellerUser());
        if (validateBook != null) {
            responseNew.setSuccess(false);
            responseNew.setCodigo("Book-01");
            responseNew.setMessage("Book already exists");
            logbook = responseNew.getCodigo() + " ISBN: " + newBookReq.getIsbn() + " Seller user" + newBookReq.getSellerUser() + " " + responseNew.getMessage();
            utilLogs.logApi(1, logbook, BookController.class.getSimpleName(), request.getRemoteAddr());
        } else {
            newBookReq.setID(newBookReq.getIsbn() + newBookReq.getSellerUser());
            final BookEntity createBook = booksService.saveNewBook(newBookReq);
            if (createBook != null) {
                responseNew.setSuccess(true);
                responseNew.setCodigo("Book-01");
                responseNew.setMessage("Book created successfully");
                responseNew.setData(newBookReq);
                logbook = responseNew.getMessage() + ": " + responseNew.getData() + " ";
            } else {
                responseNew.setSuccess(false);
                responseNew.setCodigo("ErrorBook-01");
                responseNew.setMessage("Error creating Book");
                logbook = responseNew.getCodigo() + " ISBN: " + newBookReq.getIsbn() + " sellerBook: " + newBookReq.getSellerUser();
                utilLogs.logApiError(logbook);
            }
            utilLogs.logApi(1, logbook, BookController.class.getSimpleName(), request.getRemoteAddr());
        }
        return responseNew;
    }

    @PostMapping("/book/update")
    public RespuestaApiDto<Object> updateBookAPP(@RequestBody BookEntity updateBookReq, HttpServletRequest request) {
        String logUpdate;
        RespuestaApiDto<Object> responseUpdate = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Update Book APP ISBN: " + updateBookReq.getIsbn() + " sellerBook: " + updateBookReq.getSellerUser(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final BookEntity updateBook = booksService.updateBookApp(updateBookReq);
        if (updateBook != null) {
            responseUpdate.setSuccess(true);
            responseUpdate.setCodigo("Book-03");
            responseUpdate.setMessage("Book update successfully");
            responseUpdate.setData(updateBook);
            logUpdate = responseUpdate.getMessage() + ": " + responseUpdate.getData() + " ";
        } else {
            responseUpdate.setSuccess(false);
            responseUpdate.setCodigo("ErrorBook-03");
            responseUpdate.setMessage("Error updating Book");
            logUpdate = responseUpdate.getCodigo() + " ISBN: " + updateBookReq.getIsbn() + " sellerBook: " + updateBookReq.getSellerUser();
            utilLogs.logApiError(logUpdate);
        }
        utilLogs.logApi(1, logUpdate, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseUpdate;
    }

    @PostMapping("/book/delete")
    public RespuestaApiDto<Object> deleteBookAPP(@RequestBody BookEntity deleteBookReq, HttpServletRequest request) {
        String logDelete;
        RespuestaApiDto<Object> responseDelete = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Delete Book APP ISBN: " + deleteBookReq.getIsbn() + " sellerBook: " + deleteBookReq.getSellerUser(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final boolean deleteBook = booksService.deleteByIsbnAndSeller(deleteBookReq.getIsbn(), deleteBookReq.getSellerUser());
        if (deleteBook) {
            responseDelete.setSuccess(true);
            responseDelete.setCodigo("Book-04");
            responseDelete.setMessage("Book delete successfully");
            logDelete = responseDelete.getMessage() + ": ISBN: " + deleteBookReq.getIsbn() + " sellerBook: " + deleteBookReq.getSellerUser();
        } else {
            responseDelete.setSuccess(false);
            responseDelete.setCodigo("ErrorBook-04");
            responseDelete.setMessage("Error delete Book");
            logDelete = responseDelete.getCodigo() + " ISBN: " + deleteBookReq.getIsbn() + " sellerBook: " + deleteBookReq.getSellerUser();
            utilLogs.logApiError(logDelete);
        }
        utilLogs.logApi(1, logDelete, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseDelete;
    }

    @PostMapping("/book/read/all")
    public RespuestaApiDto<Object> deleteBookAPP(HttpServletRequest request) {
        String logReadAll;
        RespuestaApiDto<Object> responseReadALL = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Read all Books APP ", BookController.class.getSimpleName(), request.getRemoteAddr());
        final List<BookEntity> readAllBooks = booksService.findall();
        if (readAllBooks != null) {
            responseReadALL.setSuccess(true);
            responseReadALL.setCodigo("Book-05");
            responseReadALL.setMessage("Books Read successfully");
            responseReadALL.setData(readAllBooks);
            logReadAll = responseReadALL.getMessage() + ": Usuarios encontrados = " + readAllBooks.size() + " ";
        } else {
            responseReadALL.setSuccess(false);
            responseReadALL.setCodigo("ErrorBook-05");
            responseReadALL.setMessage("Error Read Books");
            logReadAll = responseReadALL.getCodigo() + " " + responseReadALL.getMessage();
            utilLogs.logApiError(logReadAll);
        }
        utilLogs.logApi(1, logReadAll, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseReadALL;
    }


    @PostMapping("/book/search/isbn")
    public RespuestaApiDto<Object> searchIsbn(@RequestBody BookEntity searchReq, HttpServletRequest request) {
        String logSearchIsbn;
        RespuestaApiDto<Object> responseReadALL = new RespuestaApiDto<>(false, null, null);
        utilLogs.logApi(0, "Search Info BOOK Google isbn " + searchReq.getIsbn(), BookController.class.getSimpleName(), request.getRemoteAddr());
        final BookEntity bookmarked = booksService.getInfoIsbn(searchReq.getIsbn());
        if (Objects.equals(bookmarked.getID(), "") || bookmarked.getID() == null) {
            responseReadALL.setSuccess(true);
            responseReadALL.setCodigo("Book-06");
            responseReadALL.setMessage("Book searched successfully");
            responseReadALL.setData(bookmarked);
            logSearchIsbn = responseReadALL.getMessage() + ": Libro encontrados = " + bookmarked.getName() + " author:" + bookmarked.getAuthor();
        } else if (bookmarked.getID().equals("0")) {
            responseReadALL.setSuccess(true);
            responseReadALL.setCodigo("ErrorBook-06");
            responseReadALL.setMessage("no se obtiene Info Google BOOKS ISBN: " + searchReq.getIsbn());
            responseReadALL.setData(bookmarked);
            logSearchIsbn = responseReadALL.getMessage();
        } else {
            responseReadALL.setSuccess(false);
            responseReadALL.setCodigo("ErrorBook-06");
            responseReadALL.setMessage("Error search Book in Google BOOKS");
            logSearchIsbn = responseReadALL.getCodigo() + " " + responseReadALL.getMessage();
            utilLogs.logApiError(logSearchIsbn);
        }
        utilLogs.logApi(1, logSearchIsbn, BookController.class.getSimpleName(), request.getRemoteAddr());
        return responseReadALL;
    }

}
