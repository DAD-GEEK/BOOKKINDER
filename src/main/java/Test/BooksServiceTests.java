package Test;

import com.grootgeek.apibookkinder.entities.BookEntity;
import com.grootgeek.apibookkinder.repository.BookRepository;
import com.grootgeek.apibookkinder.service.BooksService;
import com.grootgeek.apibookkinder.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.gen5.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.gen5.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BooksServiceTests {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    Utils utils;

    BooksService booksService = new BooksService(utils, bookRepository);


    @Test
    public void testResponse() throws IOException {
        // Mock HttpsURLConnection
        HttpsURLConnection mockConnection = mock(HttpsURLConnection.class);

        // Set up a BufferedReader with a sample response
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockConnection.getInputStream()).thenReturn(Mockito.any(InputStream.class));
        when(mockReader.readLine()).thenReturn("{ \"key\": \"value\"}", null);

        // Call the method
        JSONObject result = booksService.response(mockConnection, 200);

        // Verify that the expected methods were called
        Mockito.verify(mockConnection).getInputStream();
        Mockito.verify(mockReader, Mockito.atLeastOnce()).readLine();

        // Verify the result
        assertEquals(new JSONObject("{ \"key\": \"value\"}"), result);
    }

    @Test
    public void testErrorResponse() throws IOException {
        // Mock HttpsURLConnection
        HttpsURLConnection mockConnection = mock(HttpsURLConnection.class);

        // Call the method with an error code
        JSONObject result = booksService.response(mockConnection, 404);

        // Verify that no methods were called in case of an error code
        Mockito.verifyNoInteractions(mockConnection);

        // Verify the result is null
        assertNull(result);
    }

    @Test
    public void testGenerateConnection() throws IOException {
        URL mockUrl = mock(URL.class);
        HttpsURLConnection mockConnection = mock(HttpsURLConnection.class);

        when(mockUrl.openConnection()).thenReturn(mockConnection);

        int timeout = 5000;
        HttpsURLConnection connection = (HttpsURLConnection) mockUrl.openConnection();

        connection.setConnectTimeout(timeout);

        // Verify that the URL was created with the correct parameter
        Mockito.verify(mockUrl).openConnection();
        Mockito.verify(mockConnection).setConnectTimeout(timeout);
        Mockito.verify(mockConnection).setDoOutput(true);
        Mockito.verify(mockConnection).setDoInput(true);
    }

    @Test
    public void testGetInfoIsbn_Success() throws IOException {
        // Mock HttpsURLConnection
        HttpsURLConnection mockConnection = mock(HttpsURLConnection.class);

        // Mock JSON Response
        JSONObject mockResponse = new JSONObject();
        JSONObject mockVolumeInfo = new JSONObject();
        JSONArray mockAuthors = new JSONArray();
        JSONArray mockCategories = new JSONArray();
        JSONObject mockImageLinks = new JSONObject();

        mockResponse.put("totalItems", 1);
        mockVolumeInfo.put("title", "Mock Title");
        mockAuthors.put("Mock Author");
        mockVolumeInfo.put("authors", mockAuthors);
        mockCategories.put("Mock Category");
        mockVolumeInfo.put("categories", mockCategories);
        mockVolumeInfo.put("description", "Mock Description");
        mockImageLinks.put("thumbnail", "Mock Thumbnail");
        mockVolumeInfo.put("imageLinks", mockImageLinks);
        JSONObject mockItem = new JSONObject();
        mockItem.put("volumeInfo", mockVolumeInfo);
        JSONArray mockItems = new JSONArray();
        mockItems.put(mockItem);
        mockResponse.put("items", mockItems);

        Mockito.doReturn(mockConnection).when(booksService).generateConnection(anyString());
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(booksService.response(mockConnection, 200)).thenReturn(mockResponse);

        // Call the method
        BookEntity result = booksService.getInfoIsbn("1234567890");

        // Verify the result
        assertEquals("1234567890", result.getIsbn());
        assertEquals("Mock Title", result.getName());
        assertEquals("Mock Author", result.getAuthor());
        assertEquals("Mock Category", result.getCategory());
        assertEquals("Mock Description", result.getDescription());
        assertEquals("Mock Thumbnail", result.getUrlImage());
    }

    @Test
    public void testGetInfoIsbn_Failure() throws IOException {
        Mockito.doReturn(null).when(booksService).generateConnection(anyString());

        BookEntity result = booksService.getInfoIsbn("1234567890");

        assertEquals(null, result);
    }

    @Test
    public void testDeleteByIsbnAndSeller_Success() {
        BookRepository mockRepository = mock(BookRepository.class);

        boolean result = booksService.deleteByIsbnAndSeller("1234567890", "sellerUser");
        Mockito.verify(mockRepository).deleteByIsbnAndSellerUser("1234567890", "sellerUser");
        assertTrue(result);
    }

    @Test
    public void testDeleteByIsbnAndSeller_Failure() {
        BookRepository mockRepository = mock(BookRepository.class);
        doThrow(Exception.class).when(mockRepository).deleteByIsbnAndSellerUser(anyString(), anyString());
        boolean result = booksService.deleteByIsbnAndSeller("1234567890", "sellerUser");

        assertFalse(result);
    }


    @Test
    public void testFindAll_Success() {
        // Mock BookRepository
        BookRepository mockRepository = mock(BookRepository.class);

        List<BookEntity> mockBooks = new ArrayList<>();
        mockBooks.add(new BookEntity());
        mockBooks.add(new BookEntity());
        when(mockRepository.findAll()).thenReturn(mockBooks);

        List<BookEntity> result = booksService.findall();

        assertEquals(mockBooks, result);
    }

    @Test
    public void testFindAll_Failure() {
        BookRepository mockRepository = mock(BookRepository.class);

        when(mockRepository.findAll()).thenThrow(Exception.class);

        List<BookEntity> result = booksService.findall();

        assertNull(result);
    }

    @Test
    public void testUpdateBookApp_Success() {
        BookRepository mockRepository = mock(BookRepository.class);

        BookEntity mockBook = new BookEntity();
        mockBook.setIsbn("1234567890");
        mockBook.setSellerUser("sellerUser");

        when(mockRepository.save(any(BookEntity.class))).thenReturn(mockBook);

        BookEntity updateBookApp = new BookEntity(); // Create a mock updated book
        updateBookApp.setIsbn("1234567890");
        updateBookApp.setSellerUser("sellerUser");
        BookEntity result = booksService.updateBookApp(updateBookApp);

        assertEquals(mockBook, result);
    }

    @Test
    public void testUpdateBookApp_Failure() {
        BookRepository mockRepository = mock(BookRepository.class);

        BookEntity updateBookApp = new BookEntity();
        updateBookApp.setIsbn("1234567890");
        updateBookApp.setSellerUser("sellerUser");
        BookEntity result = booksService.updateBookApp(updateBookApp);

        assertNull(result);
    }

    @Test
    public void testFindAllByIsbnAndSeller_Success() {
        BookRepository mockRepository = mock(BookRepository.class);

        BookEntity mockBook = new BookEntity();
        mockBook.setIsbn("1234567890");
        mockBook.setSellerUser("sellerUser");
        Optional<BookEntity> mockOptionalBook = Optional.of(mockBook);
        when(mockRepository.findByIsbnAndSellerUser("1234567890", "sellerUser")).thenReturn(mockOptionalBook);

        BookEntity result = booksService.findallByisbnandseller("1234567890", "sellerUser");

        assertEquals(mockBook, result);
    }

    @Test
    public void testFindAllByIsbnAndSeller_Failure() {
        BookRepository mockRepository = mock(BookRepository.class);

        when(mockRepository.findByIsbnAndSellerUser(anyString(), anyString())).thenReturn(Optional.empty());

        BookEntity result = booksService.findallByisbnandseller("1234567890", "sellerUser");

        assertNull(result);
    }

    @Test
    public void testFindAllByIsbn_Success() {
        BookRepository mockRepository = mock(BookRepository.class);

        List<BookEntity> mockBooks = new ArrayList<>();
        mockBooks.add(new BookEntity());
        mockBooks.add(new BookEntity());
        when(mockRepository.findByIsbn("1234567890")).thenReturn(mockBooks);

        List<BookEntity> result = booksService.findallByisbn("1234567890");

        assertEquals(mockBooks, result);
    }

    @Test
    public void testFindAllByIsbn_Failure() {
        BookRepository mockRepository = mock(BookRepository.class);

        when(mockRepository.findByIsbn(anyString())).thenReturn(new ArrayList<>());

        List<BookEntity> result = booksService.findallByisbn("1234567890");

        assertNull(result);
    }

    @Test
    public void testSaveNewBook_Success() {
        BookRepository mockRepository = mock(BookRepository.class);

        BookEntity mockBook = new BookEntity();
        mockBook.setIsbn("1234567890");
        mockBook.setSellerUser("sellerUser");
        when(mockRepository.save(any(BookEntity.class))).thenReturn(mockBook);

        BookEntity newBook = new BookEntity(); // Create a mock new book
        newBook.setIsbn("1234567890");
        newBook.setSellerUser("sellerUser");
        BookEntity result = booksService.saveNewBook(newBook);

        assertEquals(mockBook, result);
    }

    @Test
    public void testSaveNewBook_Failure() {
        BookRepository mockRepository = mock(BookRepository.class);

        when(mockRepository.save(any(BookEntity.class))).thenReturn(null);

        BookEntity newBook = new BookEntity(); // Create a mock new book
        newBook.setIsbn("1234567890");
        newBook.setSellerUser("sellerUser");
        BookEntity result = booksService.saveNewBook(newBook);

        assertNull(result);
    }
}
