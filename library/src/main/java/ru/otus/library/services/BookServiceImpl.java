package ru.otus.library.services;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;
import ru.otus.library.repositories.BookRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if(books.size() == 0) {
            throw new NoSuchElementException("There is no book in the library.");
        }
        books.forEach(book -> {
            Hibernate.initialize(book.getComments());
            Hibernate.initialize(book.getGenres());
            Hibernate.initialize(book.getAuthors());
        });
        return books;
    }

    @Override
    public Book findBookById(long id) {
        Optional<Book> optional = bookRepository.findById(id);
        if(optional.isEmpty()){
            throw new IllegalArgumentException("Book with this id not found.");
        }
        Book book = optional.get();
        Hibernate.initialize(book.getComments());
        Hibernate.initialize(book.getGenres());
        Hibernate.initialize(book.getAuthors());
        return book;
    }

    @Override
    public Book findBookByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        if(books.size() == 0) {
            throw new NoSuchElementException("There is no book with that title in the library.");
        }
        if(books.size() > 1) {
            throw new IllegalStateException("Several books with that title found in the library");
        }
        Book book = books.get(0);
        Hibernate.initialize(book.getComments());
        Hibernate.initialize(book.getGenres());
        Hibernate.initialize(book.getAuthors());
        return book;
    }

    @Override
    public Book addBook(Book book) {
        if(bookRepository.findById(book.getId()).isPresent()){
            throw new IllegalArgumentException("The book is already in the library.");
        }
        return bookRepository.save(book);
    }

    @Override
    public Book addBookAuthor(Book book, Author author) {
        if(bookRepository.findById(book.getId()).isEmpty()){
            throw new IllegalArgumentException("You are trying to add an author to a non-existent book.");
        }
        if(book.getAuthors().contains(author)) {
            throw new IllegalArgumentException("The book is already have this author.");
        }
        book.getAuthors().add(author);
        return bookRepository.save(book);
    }

    @Override
    public Book addBookGenre(Book book, Genre genre) {
        if(bookRepository.findById(book.getId()).isEmpty()){
            throw new IllegalArgumentException("You are trying to add a genre to a non-existent book.");
        }
        if(book.getGenres().contains(genre)) {
            throw new IllegalArgumentException("The book is already have this genre.");
        }
        book.getGenres().add(genre);
        return bookRepository.save(book);
    }

    @Override
    public Book addBookComment(Book book, Comment comment) {
        if(bookRepository.findById(book.getId()).isEmpty()){
            throw new IllegalArgumentException("You are trying to add a comment to a non-existent book.");
        }
        if(book.getComments().contains(comment)) {
            throw new IllegalArgumentException("The book is already have this comment.");
        }
        book.getComments().add(comment);
        return bookRepository.save(book);
    }

    @Override
    public Book changeBookTitle(Book book, String newTitle) {
        book.setTitle(newTitle);
        return bookRepository.save(book);
    }

    @Override
    public Book deleteBookAuthor(Book book, Author author) {
        if(bookRepository.findById(book.getId()).isEmpty()){
            throw new IllegalArgumentException("You are trying to delete an author to a non-existent book.");
        }
        if(!book.getAuthors().contains(author)) {
            throw new IllegalArgumentException("This book does not have such an author.");
        }
        book.getAuthors().remove(author);
        return bookRepository.save(book);
    }

    @Override
    public Book deleteBookGenre(Book book, Genre genre) {
        if(bookRepository.findById(book.getId()).isEmpty()){
            throw new IllegalArgumentException("You are trying to delete a genre to a non-existent book.");
        }
        if(!book.getGenres().contains(genre)) {
            throw new IllegalArgumentException("This book does not have such a genre.");
        }
        book.getGenres().remove(genre);
        return bookRepository.save(book);
    }

    @Override
    public Book deleteBookComment(Book book, Comment comment) {
        if(bookRepository.findById(book.getId()).isEmpty()){
            throw new IllegalArgumentException("You are trying to delete a genre to a non-existent book.");
        }
        if(!book.getComments().contains(comment)) {
            throw new IllegalArgumentException("This book does not have such a genre.");
        }
        book.getComments().remove(comment);
        return bookRepository.save(book);
    }

    @Override
    public Book deleteBookById(long id) {
        Book book = null;
        Optional optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            book = bookRepository.findById(id).get();
            bookRepository.deleteById(id);
        }
        return book;
    }
}
