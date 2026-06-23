package br.edu.usp.javalibrary.javalibrary.service.exceptions;

public class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
