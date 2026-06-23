package br.edu.usp.javalibrary.javalibrary.service.domains;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    public void testBookCreation() {
        Book book = new Book("9788535902777", "Dom Casmurro", "Clássico brasileiro", "Editora 34", "Machado de Assis", "Literatura", 5);
        assertEquals("9788535902777", book.getIsbn());
        assertEquals("Dom Casmurro", book.getTitle());
        assertEquals("Clássico brasileiro", book.getDescription());
        assertEquals("Editora 34", book.getPublisher());
        assertEquals("Machado de Assis", book.getAuthor());
        assertEquals("Literatura", book.getCategory());
        assertEquals(5, book.getCopiesCount());
    }

    @Test
    public void testBookCopiesModification() {
        Book book = new Book();
        book.setCopiesCount(10);
        assertEquals(10, book.getCopiesCount());
        
        book.setCopiesCount(book.getCopiesCount() - 1);
        assertEquals(9, book.getCopiesCount());
    }

    @Test
    public void testBookEquality() {
        Book book1 = new Book("123", "Title", "Desc", "Pub", "Auth", "Cat", 3);
        Book book2 = new Book("123", "Title", "Desc", "Pub", "Auth", "Cat", 3);
        Book book3 = new Book("456", "Title", "Desc", "Pub", "Auth", "Cat", 3);

        assertEquals(book1, book2, "Books with same attributes should be equal.");
        assertNotEquals(book1, book3, "Books with different ISBNs should not be equal.");
    }
}
