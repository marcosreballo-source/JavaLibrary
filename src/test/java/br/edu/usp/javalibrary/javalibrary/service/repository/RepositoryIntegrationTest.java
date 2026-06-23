package br.edu.usp.javalibrary.javalibrary.service.repository;

import br.edu.usp.javalibrary.javalibrary.service.domains.Book;
import br.edu.usp.javalibrary.javalibrary.service.domains.Loan;
import br.edu.usp.javalibrary.javalibrary.service.domains.User;
import br.edu.usp.javalibrary.javalibrary.service.exceptions.BookNotAvailableException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryIntegrationTest {

    private static final Path BOOKS_PATH = Paths.get("books.json");
    private static final Path USERS_PATH = Paths.get("users.json");
    private static final Path LOANS_PATH = Paths.get("loans.json");

    private static Path booksBackup;
    private static Path usersBackup;
    private static Path loansBackup;

    @BeforeAll
    public static void setupBackup() throws IOException {
        // Create backups of existing JSON files if they exist to protect user data
        if (Files.exists(BOOKS_PATH)) {
            booksBackup = Files.createTempFile("books_backup", ".json");
            Files.copy(BOOKS_PATH, booksBackup, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        if (Files.exists(USERS_PATH)) {
            usersBackup = Files.createTempFile("users_backup", ".json");
            Files.copy(USERS_PATH, usersBackup, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        if (Files.exists(LOANS_PATH)) {
            loansBackup = Files.createTempFile("loans_backup", ".json");
            Files.copy(LOANS_PATH, loansBackup, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @AfterAll
    public static void restoreBackup() throws IOException {
        // Restore backups to original files
        if (booksBackup != null) {
            Files.copy(booksBackup, BOOKS_PATH, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(booksBackup);
        } else {
            Files.deleteIfExists(BOOKS_PATH);
        }

        if (usersBackup != null) {
            Files.copy(usersBackup, USERS_PATH, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(usersBackup);
        } else {
            Files.deleteIfExists(USERS_PATH);
        }

        if (loansBackup != null) {
            Files.copy(loansBackup, LOANS_PATH, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(loansBackup);
        } else {
            Files.deleteIfExists(LOANS_PATH);
        }
    }

    @BeforeEach
    public void clearData() throws IOException {
        // Reset files to empty arrays for testing
        Files.writeString(BOOKS_PATH, "[]");
        Files.writeString(USERS_PATH, "[]");
        Files.writeString(LOANS_PATH, "[]");
        
        // Force repositories to reload clean files
        UserRepository.getInstance().saveUsers(new ArrayList<>());
        BookRepository.getInstance().saveBooks(new ArrayList<>());
        LoanRepository.getInstance().getLoans().clear();
        LoanRepository.getInstance().removeLoan(UUID.randomUUID()); 
    }

    @Test
    public void testUserLifecycle() {
        UserRepository repo = UserRepository.getInstance();
        User user = new User("Test User", "test@test.com", "password");
        
        assertTrue(repo.saveUser(user));
        
        Optional<User> retrieved = repo.getUser(user.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Test User", retrieved.get().getName());

        Optional<User> retrievedByEmail = repo.getUser("test@test.com");
        assertTrue(retrievedByEmail.isPresent());
        assertEquals(user.getId(), retrievedByEmail.get().getId());

        assertTrue(repo.removeUser(user.getId()));
        assertFalse(repo.getUser(user.getId()).isPresent());
    }

    @Test
    public void testBookLifecycleAndAvailability() throws BookNotAvailableException {
        BookRepository bookRepo = BookRepository.getInstance();
        Book book = new Book("978111111", "Java Testing", "Testing details", "Pub", "Author", "Education", 1);
        
        assertTrue(bookRepo.saveBook(book));
        
        Optional<Book> retrieved = bookRepo.getBook("978111111");
        assertTrue(retrieved.isPresent());
        assertEquals(1, retrieved.get().getCopiesCount());

        // Loan the book (should decrement count to 0)
        assertTrue(bookRepo.loanBook("978111111"));
        assertEquals(0, bookRepo.getBook("978111111").get().getCopiesCount());

        // Loan again (should throw BookNotAvailableException)
        assertThrows(BookNotAvailableException.class, () -> {
            bookRepo.loanBook("978111111");
        });

        // Return the book (should increment count to 1)
        assertTrue(bookRepo.returnBook("978111111"));
        assertEquals(1, bookRepo.getBook("978111111").get().getCopiesCount());
    }

    @Test
    public void testLoanRepository() {
        LoanRepository loanRepo = LoanRepository.getInstance();
        UUID userId = UUID.randomUUID();
        String isbn = "978222222";
        UUID loanId = UUID.randomUUID();
        Loan loan = new Loan(loanId, isbn, userId, LocalDateTime.now(), LocalDateTime.now().plusDays(14));

        assertTrue(loanRepo.saveLoan(loan));
        assertTrue(loanRepo.hasLoanByUserId(userId));
        assertTrue(loanRepo.hasLoanByBookIsbn(isbn));
        
        assertTrue(loanRepo.removeLoan(loanId));
        assertFalse(loanRepo.hasLoanByUserId(userId));
    }
}
