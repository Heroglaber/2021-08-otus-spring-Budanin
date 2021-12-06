package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.services.BookService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/", "/library"})
public class BookController {
    private final BookService bookService;

    @GetMapping
    public String getAll(Model model) {
        List<BookDTO> books = bookService.getAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/addBook")
    public String addForm(Model model) {
        BookDTO book = new BookDTO();
        book.addAuthor(new AuthorDTO());
        book.addGenre(new GenreDTO());
        model.addAttribute("book", book);
        return "add_form";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") BookDTO book) {
        book.getAuthors().removeIf(authorDTO -> authorDTO.getName() == null);
        book.getGenres().removeIf(genreDTO -> genreDTO.getName() == null);
        bookService.add(book);
//        if(!comment.isBlank()) {
//            addComment(bookDTO.getId(), comment);
//        }
        //commentService.add(comment);
        return "redirect:/library";
    }

    @PostMapping(value="/addBookAuthor", params={"viewName", "addBookAuthor"})
    public  String addBookAuthor(@ModelAttribute("book") BookDTO book,
                                 Model model,
                                 HttpServletRequest req) {
        final String viewName = req.getParameter("viewName");
        book.addAuthor(new AuthorDTO());
        return viewName + " :: authors";
    }

    @PostMapping(value="/removeBookAuthor", params={"viewName", "removeBookAuthor"})
    public String removeBookAuthor(
            @ModelAttribute("book") BookDTO book,
            HttpServletRequest req) {
        final String viewName = req.getParameter("viewName");
        final Integer authorFieldId = Integer.valueOf(req.getParameter("removeBookAuthor"));
        book.getAuthors().remove(authorFieldId.intValue());
        return viewName + " :: authors";
    }

    @PostMapping(value="/addBookGenre", params={"viewName", "addBookGenre"})
    public  String addBookGenre(@ModelAttribute("book") BookDTO book,
                                Model model,
                                HttpServletRequest req) {
        final String viewName = req.getParameter("viewName");
        book.addGenre(new GenreDTO());
        return viewName + " :: genres";
    }

    @PostMapping(value="/removeBookGenre", params={"viewName", "removeBookGenre"})
    public String removeBookGenre(
            @ModelAttribute("book") BookDTO book,
            HttpServletRequest req) {
        final Integer genreFieldId = Integer.valueOf(req.getParameter("removeBookGenre"));
        book.getGenres().remove(genreFieldId.intValue());
        final String viewName = req.getParameter("viewName");
        return viewName + " :: genres";
    }

    @DeleteMapping("/delete/{bookId}")
    public String deleteBook(@PathVariable("bookId") String bookId) {
        bookService.deleteById(bookId);
        return "redirect:/library";
    }

    @GetMapping("/edit/{bookId}")
    public String editForm(@PathVariable("bookId") String bookId, Model model) {
        BookDTO book = bookService.getById(bookId);
        model.addAttribute("book", book);
        return "edit_form";
    }

    @PostMapping("/edit/{bookId}")
    public String editBook(@PathVariable("bookId") String bookId, @ModelAttribute("book") BookDTO book) {
        if(!bookId.equals(book.getId())) {
            throw new RuntimeException("Book id in url not match id in model.");
        }
        book.getAuthors().removeIf(authorDTO -> authorDTO.getName() == null);
        book.getGenres().removeIf(genreDTO -> genreDTO.getName() == null);
        bookService.update(book);
        return "redirect:/library";
    }

    @InitBinder    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
