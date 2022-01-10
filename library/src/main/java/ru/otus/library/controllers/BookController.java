package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.models.dto.BookDTO;

import ru.otus.library.services.BookService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/", "/library"})
public class BookController {
    private final BookService bookService;

    @GetMapping("/book")
    public List<BookDTO> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/book/{bookId}")
    public BookDTO getBook(@PathVariable("bookId") String bookId) {
        try {
            return bookService.getById(bookId);
        }
        catch(NoSuchElementException e) {
            return null;
        }
    }

    @PostMapping("/book")
    public BookDTO addBook(@RequestBody BookDTO book) {
        book.getAuthors().removeIf(authorDTO -> authorDTO.getName() == null);
        book.getGenres().removeIf(genreDTO -> genreDTO.getName() == null);
        return bookService.add(book);
    }

    @PostMapping("/book/{bookId}")
    public BookDTO updateBook(@RequestBody BookDTO book, @PathVariable("bookId") String bookId) {
        if(!bookId.equals(book.getId())) {
            throw new RuntimeException("Book id in url not match id in model.");
        }
        book.getAuthors().removeIf(authorDTO -> authorDTO.getName() == null);
        book.getGenres().removeIf(genreDTO -> genreDTO.getName() == null);
        return bookService.update(book);
    }

    @DeleteMapping("/book/{bookId}")
    public BookDTO deleteBook(@PathVariable("bookId") String bookId) {
        try {
            return bookService.deleteById(bookId);
        }
        catch(NoSuchElementException e) {
            return null;
        }
    }
}
