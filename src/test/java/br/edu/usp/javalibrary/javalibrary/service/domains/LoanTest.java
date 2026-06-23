package br.edu.usp.javalibrary.javalibrary.service.domains;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class LoanTest {

    @Test
    public void testLoanCreationAndGetters() {
        UUID loanId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String isbn = "9781234567890";
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime endPrevision = start.plusDays(14);

        Loan loan = new Loan(loanId, isbn, userId, start, endPrevision);

        assertEquals(loanId, loan.getId());
        assertEquals(isbn, loan.getBookISBN());
        assertEquals(userId, loan.getUserID());
        assertEquals(start, loan.getStart());
        assertEquals(endPrevision, loan.getEndPrevision());
        assertNull(loan.getEnd());
        assertFalse(loan.isFinished(), "Loan should not be finished upon creation.");
    }

    @Test
    public void testLoanFinishedState() {
        UUID loanId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String isbn = "9781234567890";
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime endPrevision = start.plusDays(14);

        Loan loan = new Loan(loanId, isbn, userId, start, endPrevision);
        loan.setEnd(LocalDateTime.now());

        assertTrue(loan.isFinished(), "Loan should be finished after setting the end date.");
        assertFalse(loan.isDelayed(), "Finished loans should not be considered delayed.");
    }

    @Test
    public void testLoanDelayedState() {
        UUID loanId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String isbn = "9781234567890";
        LocalDateTime start = LocalDateTime.now().minusDays(15);
        LocalDateTime endPrevision = start.plusDays(14); // Due 1 day ago

        Loan loan = new Loan(loanId, isbn, userId, start, endPrevision);
        
        assertTrue(loan.isDelayed(), "Loan should be delayed if current time is after endPrevision and it is not finished.");
        
        // Mark as finished
        loan.setEnd(LocalDateTime.now());
        assertFalse(loan.isDelayed(), "A finished loan should no longer be delayed.");
    }
}
