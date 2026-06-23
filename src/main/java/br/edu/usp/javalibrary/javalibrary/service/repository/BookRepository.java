package br.edu.usp.javalibrary.javalibrary.service.repository;

import br.edu.usp.javalibrary.javalibrary.service.JsonService;
import br.edu.usp.javalibrary.javalibrary.service.domains.Book;
// import br.edu.usp.javalibrary.javalibrary.service.domains.User;

// import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class BookRepository {
    static final String bookFilePath = "books.json";

    private HashMap<String, Book> books;

    private static BookRepository instance;
    public static BookRepository getInstance(){
        if (instance == null) instance = new BookRepository();
        return instance;
    }

    private BookRepository(){}

    private void loadBooksFile() {
        try {
            Type listType = new TypeToken<ArrayList<Book>>(){}.getType();
            final ArrayList<Book> books = JsonService.getInstance().loadJson(bookFilePath, listType);
            this.books = new HashMap<>(books.stream().collect(Collectors.toMap(Book::getIsbn, book -> book)));
        } catch (Exception e) {
            books = new HashMap<>();
        }
    }

    private boolean saveBooksFile() {
        try {
            JsonService.getInstance().saveJson(bookFilePath, getBooks());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Book> getBooks() {
        if (books == null) loadBooksFile();
        return new ArrayList<>(books.values());
    }

    public Optional<Book> getBook(String isbn) {
        if (books == null) loadBooksFile();
        return Optional.ofNullable(books.get(isbn));
    }

    public boolean saveBooks(ArrayList<Book> books) {
        this.books = new HashMap<>(books.stream().collect(Collectors.toMap(Book::getIsbn, book -> book)));
        return saveBooksFile();
    }

    public boolean saveBook(Book book) {
        if (books == null) loadBooksFile();
        books.put(book.getIsbn(), book);
        return saveBooksFile();
    }

    public boolean removeBook(String isbn) {
        if (books == null) loadBooksFile();
        books.remove(isbn);
        return saveBooksFile();
    }

    public boolean loanBook(String isbn) throws br.edu.usp.javalibrary.javalibrary.service.exceptions.BookNotAvailableException {
        if (books == null) loadBooksFile();
        Book book = books.get(isbn);
        if (book != null && book.getCopiesCount() > 0) {
            book.setCopiesCount(book.getCopiesCount() - 1);
            return saveBooksFile();
        } 
        throw new br.edu.usp.javalibrary.javalibrary.service.exceptions.BookNotAvailableException("Não há cópias disponíveis ou o livro não foi encontrado.");
    }

    public boolean returnBook(String isbn) {
        if (books == null) loadBooksFile();
        Book book = books.get(isbn);
        if (book != null) {
            book.setCopiesCount(book.getCopiesCount() + 1);
            return saveBooksFile();
        } return false;
    }

}
