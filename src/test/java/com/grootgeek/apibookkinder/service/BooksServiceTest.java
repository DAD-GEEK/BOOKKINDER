package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.BookEntity;
import com.grootgeek.apibookkinder.repository.BookRepository;
import com.grootgeek.apibookkinder.utils.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BooksServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Logger utilLogs;

    @InjectMocks
    private BooksService booksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveNewBookSuccessfully() {
        // Given  or Arrange
        BookEntity newBook = new BookEntity();
        newBook.setIsbn("1234567890");
        newBook.setSellerUser("sellerUser");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn("1234567823231290");
        bookEntity.setSellerUser("sellerUsdsdsdsser");

        // When or Act
        when(bookRepository.save(any(BookEntity.class))).thenReturn(newBook);

        // Then or Assert
        BookEntity result = booksService.saveNewBook(newBook);
        assertNotNull(result);
        assertEquals(newBook.getIsbn(), result.getIsbn());
        assertEquals(newBook.getSellerUser(), result.getSellerUser());
        assertEquals(newBook.getSellerUser(), result.getSellerUser());
        assertThat(result).isEqualToComparingFieldByField(newBook);

        assertThat(result).as("Comprobando que el Libro creado es igual al esperado").isEqualToComparingFieldByField(bookEntity);


    }

    @Test
    void saveNewBookFailed() {
        BookEntity newBook = new BookEntity();
        newBook.setIsbn("1234567890");
        newBook.setSellerUser("sellerUser");

        when(bookRepository.save(any(BookEntity.class))).thenThrow(new RuntimeException());

        BookEntity result = booksService.saveNewBook(newBook);

        assertNull(result);
        verify(utilLogs, times(1)).logApiError(anyString());
    }

    @Test
    void findallByisbnandseller() {
    }

    @Test
    void updateBookApp() {
    }

    @Test
    void findall() {
    }

    @Test
    void deletebyIsbnandSeller() {
    }

    @Test
    void getInfoIsbn() {
    }
}