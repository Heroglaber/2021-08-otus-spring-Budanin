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

//    @GetMapping("/addBook")
//    public String addForm(Model model) {
//        BookDTO book = new BookDTO();
//        book.addAuthor(new AuthorDTO());
//        book.addGenre(new GenreDTO());
//        model.addAttribute("book", book);
//        return "add_form";
//    }
//
//    @PostMapping(value="/addBookAuthor", params={"viewName", "addBookAuthor"})
//    public  String addBookAuthor(@ModelAttribute("book") BookDTO book,
//                                 Model model,
//                                 HttpServletRequest req) {
//        final String viewName = req.getParameter("viewName");
//        book.addAuthor(new AuthorDTO());
//        return viewName + " :: authors";
//    }
//
//    @PostMapping(value="/removeBookAuthor", params={"viewName", "removeBookAuthor"})
//    public String removeBookAuthor(
//            @ModelAttribute("book") BookDTO book,
//            HttpServletRequest req) {
//        final String viewName = req.getParameter("viewName");
//        final Integer authorFieldId = Integer.valueOf(req.getParameter("removeBookAuthor"));
//        book.getAuthors().remove(authorFieldId.intValue());
//        return viewName + " :: authors";
//    }
//
//    @PostMapping(value="/addBookGenre", params={"viewName", "addBookGenre"})
//    public  String addBookGenre(@ModelAttribute("book") BookDTO book,
//                                Model model,
//                                HttpServletRequest req) {
//        final String viewName = req.getParameter("viewName");
//        book.addGenre(new GenreDTO());
//        return viewName + " :: genres";
//    }
//
//    @PostMapping(value="/removeBookGenre", params={"viewName", "removeBookGenre"})
//    public String removeBookGenre(
//            @ModelAttribute("book") BookDTO book,
//            HttpServletRequest req) {
//        final Integer genreFieldId = Integer.valueOf(req.getParameter("removeBookGenre"));
//        book.getGenres().remove(genreFieldId.intValue());
//        final String viewName = req.getParameter("viewName");
//        return viewName + " :: genres";
//    }
//
//    @DeleteMapping("/delete/{bookId}")
//    public String deleteBook(@PathVariable("bookId") String bookId) {
//        bookService.deleteById(bookId);
//        return "redirect:/library";
//    }
//
//    @GetMapping("/edit/{bookId}")
//    public String editForm(@PathVariable("bookId") String bookId, Model model) {
//        BookDTO book = bookService.getById(bookId);
//        model.addAttribute("book", book);
//        return "edit_form";
//    }
//
//    @PostMapping("/edit/{bookId}")
//    public String editBook(@PathVariable("bookId") String bookId, @ModelAttribute("book") BookDTO book) {
//        if(!bookId.equals(book.getId())) {
//            throw new RuntimeException("Book id in url not match id in model.");
//        }
//        book.getAuthors().removeIf(authorDTO -> authorDTO.getName() == null);
//        book.getGenres().removeIf(genreDTO -> genreDTO.getName() == null);
//        bookService.update(book);
//        return "redirect:/library";
//    }
//
//    @InitBinder    /* Converts empty strings into null when a form is submitted */
//    public void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
//    }
}
