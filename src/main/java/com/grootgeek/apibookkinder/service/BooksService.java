package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.BookEntity;
import com.grootgeek.apibookkinder.repository.BookRepository;
import com.grootgeek.apibookkinder.service.interfaces.BooksServiceInterface;
import com.grootgeek.apibookkinder.utils.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

@Service
public class BooksService implements BooksServiceInterface {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BooksService.class);

    private final Logger utilLogs;
    private final BookRepository bookRepository;

    @Value("${googleBooks.url}")
    private String googleBooksUrl;
    @Value("${googleBooks.timeout}")
    private int timeout;
    @Value("${book.seller}")
    private String sellerBook;
    @Value("${book.isbn}")
    private String isbnBook;

    @Autowired
    public BooksService(Logger utilLogs, BookRepository bookRepository) {
        this.utilLogs = utilLogs;
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity saveNewBook(BookEntity newBook) {
        try {
            BookEntity saveNewBook = bookRepository.save(newBook);
            if (saveNewBook.getIsbn().equals(newBook.getIsbn()) && saveNewBook.getSellerUser().equals(newBook.getSellerUser())) {
                return saveNewBook;
            } else {
                utilLogs.logApiError("Error Save New Book " + saveNewBook.getIsbn() + sellerBook + saveNewBook.getSellerUser());
                return null;
            }

        } catch (Exception e) {
            String message = "Error Save New Book " + newBook.getIsbn() + sellerBook + newBook.getSellerUser() + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }

    }

    @Override // method for search by isbn and seller
    public BookEntity findallByisbnandseller(String isbn, String sellerUser) {
        try {
            Optional<BookEntity> bookGet = bookRepository.findByIsbnAndSellerUser(isbn, sellerUser);
            if (bookGet.isPresent()) {
                return new BookEntity(bookGet.get());
            } else {
                utilLogs.logApiError("Books not found for "+isbnBook + isbn + sellerBook + sellerUser);
                return null;
            }
        } catch (Exception e) {
            String message = "Error find Books for "+isbnBook + isbn + sellerBook + sellerUser + " " + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }
    }


    @Override
    public BookEntity updateBookApp(BookEntity updateBookApp) {
        BookEntity bookUpdate = findallByisbnandseller(updateBookApp.getIsbn(), updateBookApp.getSellerUser());
        if (bookUpdate == null) {
            utilLogs.logApiError("Error Update  Book " + updateBookApp.getIsbn() + sellerBook + updateBookApp.getSellerUser());
            return null;
        }
        updateIfNotNullOrEmpty(bookUpdate::setId, updateBookApp.getId());
        updateIfNotNullOrEmpty(bookUpdate::setIsbn, updateBookApp.getIsbn());
        updateIfNotNullOrEmpty(bookUpdate::setName, updateBookApp.getName());
        updateIfNotNullOrEmpty(bookUpdate::setAuthor, updateBookApp.getAuthor());
        updateIfNotNullOrEmpty(bookUpdate::setCategory, updateBookApp.getCategory());
        updateIfNotNullOrEmpty(bookUpdate::setSellerUser, updateBookApp.getSellerUser());
        updateIfNotNullOrEmpty(bookUpdate::setQuality, updateBookApp.getQuality());
        updateIfNotNullOrEmpty(bookUpdate::setPrice, updateBookApp.getPrice());
        updateIfNotNullOrEmpty(bookUpdate::setQuantity, updateBookApp.getQuantity());
        updateIfNotNullOrEmpty(bookUpdate::setDescription, updateBookApp.getDescription());
        updateIfNotNullOrEmpty(bookUpdate::setObservations, updateBookApp.getObservations());
        updateIfNotNullOrEmpty(bookUpdate::setOnSale, updateBookApp.getOnSale());
        updateIfNotNullOrEmpty(bookUpdate::setUrlImage, updateBookApp.getUrlImage());
        BookEntity updater = bookRepository.save(bookUpdate);
        if (updater.getIsbn().equals(bookUpdate.getIsbn()) && updater.getSellerUser().equals(bookUpdate.getSellerUser())) {
            return updater;
        } else {
            utilLogs.logApiError("Error Update  Book " + updater.getIsbn() + sellerBook + updater.getSellerUser());
            return null;
        }
    }

    private void updateIfNotNullOrEmpty(Consumer<String> setter, String value) {
        if (Objects.nonNull(value) && !"".equalsIgnoreCase(value)) {
            setter.accept(value);
        }
    }

    @Override
    public List<BookEntity> findall() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            String message = "Error read All Books: " + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return Collections.emptyList();
        }
    }

    @Override
    public Boolean deletebyIsbnandSeller(String isbn, String sellerUser) {
        try {
            bookRepository.deleteByIsbnAndSellerUser(isbn, sellerUser);
            return true;
        } catch (Exception e) {
            String message = "Error delete Book: "+isbnBook + isbn + sellerBook + sellerUser + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return false;
        }
    }

    @Override
    public BookEntity getInfoIsbn(String isbn) {

        String url = googleBooksUrl + isbn;
        try {
            HttpsURLConnection connections = generateConnection(url);
            connections.setRequestMethod("GET");
            int responseCode = connections.getResponseCode();
            if (responseCode == 200) {
                JSONObject jsResponse = response(connections, responseCode);
                assert jsResponse != null;
                if (jsResponse.getInt("totalItems") == 0) {
                    BookEntity response = new BookEntity();
                    response.setId("0");
                    response.setIsbn(isbn);
                    return response;
                } else if ( jsResponse.getInt("totalItems") > 0){
                    BookEntity response = new BookEntity(jsResponse);
                    response.setIsbn(isbn);
                    return response;
                }

            } else {
                String message = "Error getting Info Google BOOKS " +isbn  + isbn + "Error code: " + responseCode;
                utilLogs.logApiError(message);
                logger.info(message);
                return null;
            }
            connections.disconnect();
        } catch (JSONException | IOException e) {
            String message = "Error getting Info Google BOOKS " +isbn  + isbn + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(message);
            return null;
        }
        return null;
    }

    HttpsURLConnection generateConnection(String urls) throws IOException {
        URL url = new URL(urls.trim());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        return connection;
    }

    JSONObject response(HttpsURLConnection connections, int code) {
        JSONObject jsResponse = null;
        StringBuilder resultado = new StringBuilder();
        String response;
        if (code == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader((connections.getInputStream())))) {
                while ((response = br.readLine()) != null) {
                    resultado.append(response);
                }
                jsResponse = new JSONObject(resultado.toString());
            } catch (IOException e) {
                String message = "Error getting response Info Google BOOKS " + e.getMessage();
                utilLogs.logApiError(message);
                logger.info(String.valueOf(e));
                return null;
            }
        }
        return jsResponse;
    }

}
