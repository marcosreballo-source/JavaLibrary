package br.edu.usp.javalibrary.javalibrary.service.domains;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Loan {
    private UUID id;
    private String isbn;
    private UUID userID;
    private LocalDateTime start;
    private LocalDateTime endPrevision;
    private LocalDateTime end;

    public Loan(UUID id, String isbn, UUID userID, LocalDateTime start, LocalDateTime endPrevision) {
        this.id = id;
        this.isbn = isbn;
        this.userID = userID;
        this.start = start;
        this.endPrevision = endPrevision;
        this.end = null;
    }

    public Loan(UUID id, String isbn, UUID userID, LocalDateTime start, LocalDateTime endPrevision, LocalDateTime end) {
        this.id = id;
        this.isbn = isbn;
        this.userID = userID;
        this.start = start;
        this.endPrevision = endPrevision;
        this.end = end;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBookISBN() {
        return isbn;
    }

    public void setBookISBN(String isbn) {
        this.isbn = isbn;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEndPrevision() {
        return endPrevision;
    }

    public void setEndPrevision(LocalDateTime endPrevision) {
        this.endPrevision = endPrevision;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean isDelayed() {
        return end == null && LocalDateTime.now().isAfter(endPrevision);
    }

    public boolean isFinished() {
        return end != null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final Loan other = (Loan) object;
        return id.equals(other.id) && userID.equals(other.userID) && isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, userID);
    }
}
