package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.BookEntity;
import com.grootgeek.apibookkinder.repository.BookRepository;
import com.grootgeek.apibookkinder.service.interfaces.BooksServiceInterface;
import com.grootgeek.apibookkinder.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
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

@Service
public class BooksService implements BooksServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(BooksService.class);

    private final Utils utilLogs;
    private final BookRepository bookRepository;

    @Value("${googlebooks.url}")
    private String googlebooksurl;
    @Value("${googlebooks.timeout}")
    private int timeout;

    @Autowired
    public BooksService(Utils utilLogs, BookRepository bookRepository) {
        this.utilLogs = utilLogs;
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity saveNewBook(BookEntity newbook) {
        try {
            BookEntity newBook = bookRepository.save(newbook);
            if (newBook.getIsbn().equals(newbook.getIsbn()) && newBook.getSellerUser().equals(newbook.getSellerUser())) {
                return newBook;
            } else {
                utilLogs.logApiError("Error Save New Book " + newBook.getIsbn() + " selleruser:" + newBook.getSellerUser());
                return null;
            }

        } catch (Exception e) {
            String message = "Error Save New Book " + newbook.getIsbn() + " selleruser:" + newbook.getSellerUser() + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }

    }

    @Override
    public List<BookEntity> findallByisbn(String isbn) {
        try {
            List<BookEntity> BookGet = bookRepository.findByIsbn(isbn);
            if (!BookGet.isEmpty()) {
                return BookGet;
            } else {
                utilLogs.logApiError("Books not Found for ISBN " + isbn);
                return null;
            }
        } catch (Exception e) {
            String message = "Error Find Books for ISBN: " + isbn + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }
    }

    @Override
    public BookEntity findallByisbnandseller(String isbn, String sellerUser) {
        try {
            Optional<BookEntity> bookGet = bookRepository.findByIsbnAndSellerUser(isbn, sellerUser);
            if (bookGet.isPresent() && isbn.equals(bookGet.get().getIsbn()) && sellerUser.equals(bookGet.get().getSellerUser())) {
                return new BookEntity(bookGet.get().getID(), bookGet.get().getIsbn(), bookGet.get().getName(), bookGet.get().getAuthor(), bookGet.get().getCategory(), bookGet.get().getSellerUser(), bookGet.get().getQuality(), bookGet.get().getPrice(), bookGet.get().getQuantity(), bookGet.get().getDescription(), bookGet.get().getObservations(), bookGet.get().getOnSale(), bookGet.get().getUrlImage());
            } else {
                utilLogs.logApiError("Books not Found for ISBN " + isbn + " SellerUser: " + sellerUser);
                return null;
            }
        } catch (Exception e) {
            String message = "Error Find Books for isbn: " + isbn + " SellerUser: " + sellerUser + " " + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }
    }


    @Override
    public BookEntity updateBookApp(BookEntity updateBookApp) {
        BookEntity bookUpdate = findallByisbnandseller(updateBookApp.getIsbn(), updateBookApp.getSellerUser());
        if (Objects.nonNull(updateBookApp.getID()) && !"".equalsIgnoreCase(updateBookApp.getID())) {
            bookUpdate.setID(updateBookApp.getID());
        }
        if (Objects.nonNull(updateBookApp.getIsbn()) && !"".equalsIgnoreCase(updateBookApp.getIsbn())) {
            bookUpdate.setIsbn(updateBookApp.getIsbn());
        }
        if (Objects.nonNull(updateBookApp.getName()) && !"".equalsIgnoreCase(updateBookApp.getName())) {
            bookUpdate.setName(updateBookApp.getName());
        }
        if (Objects.nonNull(updateBookApp.getAuthor()) && !"".equalsIgnoreCase(updateBookApp.getAuthor())) {
            bookUpdate.setAuthor(updateBookApp.getAuthor());
        }
        if (Objects.nonNull(updateBookApp.getCategory()) && !"".equalsIgnoreCase(updateBookApp.getCategory())) {
            bookUpdate.setCategory(updateBookApp.getCategory());
        }
        if (Objects.nonNull(updateBookApp.getSellerUser()) && !"".equalsIgnoreCase(updateBookApp.getSellerUser())) {
            bookUpdate.setSellerUser(updateBookApp.getSellerUser());
        }
        if (Objects.nonNull(updateBookApp.getQuality()) && !"".equalsIgnoreCase(updateBookApp.getQuality())) {
            bookUpdate.setQuality(updateBookApp.getQuality());
        }
        if (Objects.nonNull(updateBookApp.getPrice()) && !"".equalsIgnoreCase(updateBookApp.getPrice())) {
            bookUpdate.setPrice(updateBookApp.getPrice());
        }
        if (Objects.nonNull(updateBookApp.getQuantity()) && !"".equalsIgnoreCase(updateBookApp.getQuantity())) {
            bookUpdate.setQuantity(updateBookApp.getQuantity());
        }
        if (Objects.nonNull(updateBookApp.getDescription()) && !"".equalsIgnoreCase(updateBookApp.getDescription())) {
            bookUpdate.setDescription(updateBookApp.getDescription());
        }
        if (Objects.nonNull(updateBookApp.getObservations()) && !"".equalsIgnoreCase(updateBookApp.getObservations())) {
            bookUpdate.setObservations(updateBookApp.getObservations());
        }
        if (Objects.nonNull(updateBookApp.getOnSale()) && !"".equalsIgnoreCase(updateBookApp.getOnSale())) {
            bookUpdate.setOnSale(updateBookApp.getOnSale());
        }
        if (Objects.nonNull(updateBookApp.getUrlImage()) && !"".equalsIgnoreCase(updateBookApp.getUrlImage())) {
            bookUpdate.setUrlImage(updateBookApp.getUrlImage());
        }
        BookEntity updater = bookRepository.save(bookUpdate);
        if (updater.getIsbn().equals(bookUpdate.getIsbn()) && updater.getSellerUser().equals(bookUpdate.getSellerUser())) {
            return updater;
        } else {
            utilLogs.logApiError("Error Update  Book " + updater.getIsbn() + " seller user: " + updater.getSellerUser());
            return null;
        }
    }

    @Override
    public List<BookEntity> findall() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            String message = "Error read ALL Books: " + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }
    }

    @Override
    public Boolean deleteByIsbnAndSeller(String isbn, String sellerUser) {
        try {
            bookRepository.deleteByIsbnAndSellerUser(isbn, sellerUser);
            return true;
        } catch (Exception e) {
            String message = "Error delete Book: isbn" + isbn + " seller user" + sellerUser + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return false;
        }
    }

    @Override
    public BookEntity getInfoIsbn(String isbn) {
        BookEntity response = new BookEntity();
        String url  = googlebooksurl + isbn;
        try {
            HttpsURLConnection connections = generateConnection(url);
            connections.setRequestMethod("GET");
            int responseCode = connections.getResponseCode();
            JSONObject jsResponse;
            if (responseCode == 200) {
                jsResponse = response(connections, responseCode);
                response.setIsbn(isbn);
                if (jsResponse.getInt("totalItems")==0) {
                    String message = "no se obtiene Info Google BOOKS ISBN: " + isbn ;
                    response.setID("0");
                } else if (jsResponse.getInt("totalItems")>0) {
                    response.setName(jsResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("title"));
                    response.setAuthor(jsResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("authors").getString(0));
                    response.setCategory(jsResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("categories").getString(0));
                    response.setDescription(jsResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("description"));
                    response.setUrlImage(jsResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));
                }

            } else {
                String message = "Error obteniendo Info Google BOOKS ISBN: " + isbn + "Erorr code: " + responseCode;
                utilLogs.logApiError(message);
                logger.info(String.valueOf(responseCode));
                return null;
            }
            connections.disconnect();
        } catch (JSONException | IOException e) {
            String message = "Error obteniendo Info Google BOOKS ISBN: " + isbn + e.getMessage();
            utilLogs.logApiError(message);
            logger.info(String.valueOf(e));
            return null;
        }
        return response;
    }

    private HttpsURLConnection generateConnection(String urls) throws IOException {
        URL url = new URL(urls.trim());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        return connection;
    }

    private JSONObject response(HttpsURLConnection connections, int code) {
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
                String message = "Error obteniendo response Info Google BOOKS " + e.getMessage();
                utilLogs.logApiError(message);
                logger.info(String.valueOf(e));
                return null;
            }
        }
        return jsResponse;
    }

}
