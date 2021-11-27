package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/", "/library"})
public class BookController {
    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping
    public String getAll(Model model) {
        List<BookDTO> books = bookService.getAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/addBook")
    public String addBook(Model model) {
        BookDTO book = new BookDTO();
        book.addAuthor(new AuthorDTO());
        book.addGenre(new GenreDTO());
        model.addAttribute("book", book);
        return "book_form";
    }

    @PostMapping("/addBookAuthor")
    public  String addBookAuthor(@ModelAttribute("book") BookDTO book, Model model) {
        book.addAuthor(new AuthorDTO());
        return "book_form :: authors";
    }

    @PostMapping(value="/removeBookAuthor", params={"removeBookAuthor"})
    public String removeBookAuthor(
            @ModelAttribute("book") BookDTO book,
            HttpServletRequest req) {
        final Integer authorFieldId = Integer.valueOf(req.getParameter("removeBookAuthor"));
        book.getAuthors().remove(authorFieldId.intValue());
        return "book_form :: authors";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") BookDTO book) {
        book.getAuthors().removeIf(authorDTO -> authorDTO.getName().isBlank());
        book.getGenres().removeIf(genreDTO -> genreDTO.getName().isBlank());
        bookService.add(book);
//        if(!comment.isBlank()) {
//            addComment(bookDTO.getId(), comment);
//        }
        //commentService.add(comment);
        return "redirect:/library";
    }
}
