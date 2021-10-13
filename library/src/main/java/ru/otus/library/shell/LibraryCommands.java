package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class LibraryCommands {
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;

    @ShellMethod(value = "List of all books.", key = {"list", "l", "all"})
    String getAllBooks() {
        List<Book> books = bookService.findAll();
        return books.toString();
    }

    @ShellMethod(value = "Find a book. usage: find --title 'New Book Title'", key = {"find", "f"})
    String findBookByTitle(@ShellOption(defaultValue = "0") long id,
                           @ShellOption(defaultValue = "") String title) {
        if(id != 0) {
            Book book = bookService.findById(id);
            if(book != null) {
                return book.toString();
            }
        }
        if(title != "") {
            Book book = bookService.findByTitle(title);
            if(book != null) {
                return book.toString();
            }
        }
        return "Book not found in library";
    }

    @ShellMethod(value = "Save book. usage: i --title 'New Book Title' --genre 'classic' --author 'First Author'", key = {"insert", "i"})
    String insertBook(@ShellOption String title,
                      @ShellOption(value = "--genre", defaultValue = "") String genreName,
                      @ShellOption(value = "--author", defaultValue = "") String authorName) {
        Book book = new Book();
        book.setTitle(title);
        if(!genreName.isEmpty()) {
            Genre genre = genreService.add(new Genre(genreName));
            book.setGenre(genre);
        }
        if(!authorName.isEmpty()) {
            Author author = authorService.add(new Author(authorName));
            book.setAuthors(List.of(author));
        }
        bookService.add(book);
        return "Book successfully added to library";
    }

    @ShellMethod(value = "Update book. usage: u --id 3 --title 'Book Title' --genre 'fantasy' --author 'Second Author'", key = {"update","u"})
    String updateBook(@ShellOption long id,
                      @ShellOption(value = "--title", defaultValue = "") String title,
                      @ShellOption(value = "--genre", defaultValue = "") String genreName,
                      @ShellOption(value = "--author", defaultValue = "") String authorName) {
        Book book = bookService.findById(id);
        if(!title.isEmpty()) {
            book.setTitle(title);
        }
        if(!genreName.isEmpty()) {
            Genre genre = new Genre(genreName);
            if(!book.getGenre().equals(genre)) {
                book.setGenre(genre);
            }
        }
        if(!authorName.isEmpty()) {
            Author author = new Author(authorName);
            ArrayList<Author> authors = new ArrayList<>();
            authors.add(author);
            if(!book.getAuthors().contains(author)) {
                book.setAuthors(authors);
            }
        }
        bookService.update(book);
        return "Book successfully updated";
    }

    @ShellMethod(value = "Add or delete book author. usage: author --bookId 3 --name 'Third Author'", key = {"author", "a"})
    String addAuthor(@ShellOption(value = "--delete", defaultValue = "false") boolean delete,
                     @ShellOption("--bookId") long id,
                     @ShellOption("--name") String name) {
        Book book = bookService.findById(id);
        if(delete) {
            Optional<Author> authorOpt = book.getAuthors().stream().
                    filter(a -> a.getName().equals(name)).
                    findFirst();
            if(authorOpt.isEmpty()) {
                throw new IllegalArgumentException("Book has not such author.");
            }
            bookService.deleteAuthor(book, authorOpt.get());
            return "Author was successfully deleted.";
        }
        else {
            Author author = new Author(name);
            bookService.addAuthor(book, author);
            return "Author was added successfully";
        }
    }

    @ShellMethod(value = "Delete book by title or by id. usage: d --title 'Book Title'", key = {"d","delete"})
    String deleteBook(@ShellOption(value = "--title", defaultValue = "") String title,
                      @ShellOption(value = "--id", defaultValue = "-1") long id) {
        if(id > 0) {
            Book book = bookService.findById(id);
            bookService.delete(book);
            return "Book successfully removed from library";
        }
        if(!title.isEmpty()) {
            Book book = bookService.findByTitle(title);
            bookService.delete(book);
            return "Book successfully removed from library";
        }
        return "Wrong parameters combination.";
    }
}
