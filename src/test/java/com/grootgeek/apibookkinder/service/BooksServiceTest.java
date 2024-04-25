package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.BookEntity;
import com.grootgeek.apibookkinder.repository.BookRepository;
import com.grootgeek.apibookkinder.utils.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

   public static final String isbn = "1234567890";
    @Test
    void saveNewBookSuccessfully() {
        // Given  or Arrange
        BookEntity newBook = new BookEntity();
        newBook.setIsbn(isbn);
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
        assertThat(result).isEqualTo(newBook);

        assertThat(result).as("Comprobando que el Libro creado es igual al esperado").isNotEqualTo(bookEntity);


    }

    @Test
    void saveNewBookFailed() {
        BookEntity newBook = new BookEntity();
        newBook.setIsbn(isbn);
        newBook.setSellerUser("sellerUser");

        when(bookRepository.save(any(BookEntity.class))).thenThrow(new RuntimeException());

        BookEntity result = booksService.saveNewBook(newBook);

        assertNull(result);
        verify(utilLogs, times(1)).logApiError(anyString());
    }

    @Test
    void saveNewBookFailedDueToMismatchedIsbn() {
        BookEntity newBook = new BookEntity();
        newBook.setIsbn(isbn);
        newBook.setSellerUser("sellerUser");

        BookEntity savedBook = new BookEntity();
        savedBook.setIsbn("0987654321");
        savedBook.setSellerUser("sellerUser");

        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedBook);

        BookEntity result = booksService.saveNewBook(newBook);

        assertThat(result).isNull();
        verify(utilLogs, times(1)).logApiError(anyString());
    }

    @Test
    void saveNewBookFailedDueToMismatchedSellerUser() {
        BookEntity newBook = new BookEntity();
        newBook.setIsbn(isbn);
        newBook.setSellerUser("sellerUser");

        BookEntity savedBook = new BookEntity();
        savedBook.setIsbn(isbn);
        savedBook.setSellerUser("differentUser");

        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedBook);

        BookEntity result = booksService.saveNewBook(newBook);

        assertThat(result).isNull();
        verify(utilLogs, times(1)).logApiError(anyString());
    }

    @Test
void findBookByIsbnAndSellerSuccessfully() {
    BookEntity existingBook = new BookEntity();
    existingBook.setIsbn(isbn);
    existingBook.setSellerUser("sellerUser");

    when(bookRepository.findByIsbnAndSellerUser(anyString(), anyString())).thenReturn(Optional.of(existingBook));

    BookEntity result = booksService.findallByisbnandseller(isbn, "sellerUser");

    assertThat(result).isNotNull();
    assertThat(result.getIsbn()).isEqualTo(existingBook.getIsbn());
    assertThat(result.getSellerUser()).isEqualTo(existingBook.getSellerUser());
}

@Test
void findBookByIsbnAndSellerFailedDueToNonExistingBook() {
    when(bookRepository.findByIsbnAndSellerUser(anyString(), anyString())).thenReturn(Optional.empty());

    BookEntity result = booksService.findallByisbnandseller(isbn, "sellerUser");

    assertThat(result).isNull();
    verify(utilLogs, times(1)).logApiError(anyString());
}

@Test
void updateBookSuccessfully() {
    BookEntity existingBook = new BookEntity();
    existingBook.setIsbn(isbn);
    existingBook.setSellerUser("sellerUser");

    BookEntity updateBook = new BookEntity();
    updateBook.setIsbn(isbn);
    updateBook.setSellerUser("sellerUser");
    updateBook.setName("Updated Name");

    when(bookRepository.findByIsbnAndSellerUser(anyString(), anyString())).thenReturn(Optional.of(existingBook));
    when(bookRepository.save(any(BookEntity.class))).thenReturn(updateBook);

    BookEntity result = booksService.updateBookApp(updateBook);

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo(updateBook.getName());
}

@Test
void updateBookFailedDueToNonExistingBook() {
    BookEntity updateBook = new BookEntity();
    updateBook.setIsbn(isbn);
    updateBook.setSellerUser("sellerUser");
    BookEntity existingBook = new BookEntity();
    existingBook.setIsbn(isbn);
    when(bookRepository.findByIsbnAndSellerUser(anyString(), anyString())).thenReturn(Optional.of(updateBook));
    when(bookRepository.save(any(BookEntity.class))).thenReturn(null);
    BookEntity result = booksService.updateBookApp(existingBook);

    assertThat(result).isNull();
    verify(utilLogs, times(2)).logApiError(anyString());
}

@Test
void deleteBookByIsbnAndSellerSuccessfully() {
    doNothing().when(bookRepository).deleteByIsbnAndSellerUser(anyString(), anyString());

    Boolean result = booksService.deletebyIsbnandSeller(isbn, "sellerUser");

    assertThat(result).isTrue();
}

@Test
void deleteBookByIsbnAndSellerFailedDueToException() {
    doThrow(new RuntimeException()).when(bookRepository).deleteByIsbnAndSellerUser(anyString(), anyString());

    Boolean result = booksService.deletebyIsbnandSeller(isbn, "sellerUser");

    assertThat(result).isFalse();
    verify(utilLogs, times(1)).logApiError(anyString());
}

@Test
void findAllBooksSuccessfully() {
    List<BookEntity> books = new ArrayList<>();
    books.add(new BookEntity());

    when(bookRepository.findAll()).thenReturn(books);

    List<BookEntity> result = booksService.findall();

    assertThat(result).isNotEmpty();
}

@Test
void findAllBooksFailedDueToException() {
    when(bookRepository.findAll()).thenThrow(new RuntimeException());

    List<BookEntity> result = booksService.findall();

    assertThat(result).isEmpty();
    verify(utilLogs, times(1)).logApiError(anyString());
}
}