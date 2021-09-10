package ru.otus.library.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dto.BookDto;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;
import ru.otus.library.repositories.BookRepository;
import ru.otus.library.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public BookServiceImpl(BookRepository bookRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        List<BookDto> bookDtos = new ArrayList<>();

        List<Book> books = bookRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        if(books.size() == 0) {
            throw new NoSuchElementException("There is no book in the library.");
        }
        books.forEach(book -> {
            BookDto bookDto = mapToBookDto(book);
            List<Comment> bookComments = comments.stream()
                    .filter(comment -> {return comment.getBook().getId() == book.getId();}).collect(Collectors.toList());
            bookDto.setComments(bookComments);
            bookDtos.add(bookDto);
        });
        return bookDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findBookById(long id) {
        Optional<Book> optional = bookRepository.findById(id);
        if(optional.isEmpty()){
            throw new IllegalArgumentException("Book with this id not found.");
        }
        Book book = optional.get();
        List<Comment> comments = commentRepository.findByBook(book);

        BookDto bookDto = mapToBookDto(book);
        bookDto.setComments(comments);
        return bookDto;
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findBookByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        if(books.size() == 0) {
            throw new NoSuchElementException("There is no book with that title in the library.");
        }
        if(books.size() > 1) {
            throw new IllegalStateException("Several books with that title found in the library");
        }
        Book book = books.get(0);
        List<Comment> comments = commentRepository.findByBook(book);

        BookDto bookDto = mapToBookDto(book);
        bookDto.setComments(comments);
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto addBook(BookDto bookDto) {
        Book book = bookDto.extractBook();
        if(bookRepository.findById(book.getId()).isPresent()){
            throw new IllegalArgumentException("The book is already in the library.");
        }
        book = bookRepository.save(book);
        for(Comment comment: bookDto.getComments()) {
            comment.setBook(book);
            commentRepository.save(comment);
        }
        bookDto.setId(book.getId());
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto addBookAuthor(BookDto bookDto, Author author) {
        Optional<Book> optional = bookRepository.findById(bookDto.getId());
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to add an author to a non-existent book.");
        }
        Book book = optional.get();
        if(book.getAuthors().contains(author)) {
            throw new IllegalArgumentException("The book is already have this author.");
        }
        book.getAuthors().add(author);
        book = bookRepository.save(book);

        bookDto.setAuthors(book.getAuthors());
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto addBookGenre(BookDto bookDto, Genre genre) {
        Optional<Book> optional = bookRepository.findById(bookDto.getId());
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to add a genre to a non-existent book.");
        }
        Book book = optional.get();
        if(book.getGenres().contains(genre)) {
            throw new IllegalArgumentException("The book is already have this genre.");
        }
        book.getGenres().add(genre);
        book = bookRepository.save(book);

        bookDto.setGenres(book.getGenres());
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto addBookComment(BookDto bookDto, Comment comment) {
        Optional<Book> optional = bookRepository.findById(bookDto.getId());
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to add a comment to a non-existent book.");
        }
        Book book = optional.get();
        comment.setBook(book);

        commentRepository.save(comment);

        bookDto.getComments().add(comment);
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto changeBookTitle(BookDto bookDto, String newTitle) {
        Optional<Book> optional = bookRepository.findById(bookDto.getId());
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to change title to a non-existent book.");
        }
        Book book = optional.get();
        book.setTitle(newTitle);
        book = bookRepository.save(book);

        bookDto.setTitle(book.getTitle());
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto deleteBookAuthor(BookDto bookDto, Author author) {
        Optional<Book> optional = bookRepository.findById(bookDto.getId());
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to delete an author to a non-existent book.");
        }
        Book book = optional.get();
        if(!book.getAuthors().contains(author)) {
            throw new IllegalArgumentException("This book does not have such an author.");
        }
        book.getAuthors().remove(author);
        book = bookRepository.save(book);

        bookDto.setAuthors(book.getAuthors());
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto deleteBookGenre(BookDto bookDto, Genre genre) {
        Optional<Book> optional = bookRepository.findById(bookDto.getId());
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to delete a genre to a non-existent book.");
        }
        Book book = optional.get();
        if(!book.getGenres().contains(genre)) {
            throw new IllegalArgumentException("This book does not have such a genre.");
        }
        book.getGenres().remove(genre);
        book = bookRepository.save(book);

        bookDto.setGenres(book.getGenres());
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto deleteBookComment(BookDto bookDto, Comment comment) {
        Optional<Book> optional = bookRepository.findById(bookDto.getId());
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to delete a comment to a non-existent book.");
        }
        if(!bookDto.getComments().contains(comment)) {
            throw new IllegalArgumentException("Book does not contains this comment");
        }
        commentRepository.deleteById(comment.getId());

        bookDto.getComments().remove(comment);
        return bookDto;
    }

    @Override
    @Transactional
    public BookDto deleteBookById(long id) {
        Optional<Book> optional = bookRepository.findById(id);
        if(optional.isEmpty()){
            throw new IllegalArgumentException("You are trying to delete a non-existent book.");
        }
        Book book = optional.get();
        List<Comment> comments = commentRepository.findByBook(book);
        for(Comment comment: comments) {
            commentRepository.deleteById(comment.getId());
        }
        bookRepository.deleteById(id);
        BookDto bookDto = mapToBookDto(book);
        bookDto.setComments(comments);
        return bookDto;
    }

    private BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthors(book.getAuthors());
        bookDto.setGenres(book.getGenres());
        return bookDto;
    }
}
