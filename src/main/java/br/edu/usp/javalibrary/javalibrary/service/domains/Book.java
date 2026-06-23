package br.edu.usp.javalibrary.javalibrary.service.domains;

import java.util.Objects;

public class Book {
    String isbn;
    String title;
    String description;
    String publisher;
    String author;
    String category;
    int copiesCount;

    public Book(String isbn, String title, String description, String publisher, String author, String category, int copiesCount) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.publisher = publisher;
        this.author = author;
        this.category = category;
        this.copiesCount = copiesCount;
    }

    public Book() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCopiesCount() {
        return copiesCount;
    }

    public void setCopiesCount(int copiesCount) {
        this.copiesCount = copiesCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Book other = (Book) obj;

        return this.isbn.trim().equalsIgnoreCase(other.isbn.trim()) &&
                this.title.trim().equalsIgnoreCase(other.title.trim()) &&
                this.description.trim().equalsIgnoreCase(other.description.trim()) &&
                this.publisher.trim().equalsIgnoreCase(other.publisher.trim()) &&
                this.category.trim().equalsIgnoreCase(other.category.trim()) &&
                this.author.trim().equalsIgnoreCase(other.author.trim()) &&
                this.copiesCount == other.copiesCount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

}
