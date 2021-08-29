package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.dto.BookDTO;
import ru.otus.library.service.BookService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class LibraryCommands {
    private final BookService bookService;

    @ShellMethod(value = "Save book. usage: i --title 'New Book Title' --genre 'classic' --author 'First Author'", key = {"insert", "i"})
    String insertBook(@ShellOption String title,
                      @ShellOption String genre,
                      @ShellOption String author) {
        BookDTO book = new BookDTO(title, genre, List.of(author));
        bookService.addBook(book);
        return "Book successfully added to library";
    }

    @ShellMethod(value = "Find a book by title. usage: find --title 'New Book Title'", key = {"find", "f"})
    String findBookByTitle(@ShellOption String title) {
        BookDTO book = bookService.findByTitle(title);
        return book.toString();
    }

    @ShellMethod(value = "Update book. usage: u --id 3 --title 'Book Title' --genre 'fantasy' --author 'Second Author'", key = {"update","u"})
    String updateBook(@ShellOption long id,
                      @ShellOption String title,
                      @ShellOption String genre,
                      @ShellOption String author) {
        bookService.updateBook(new BookDTO(id, title, genre, List.of(author)));
        return "Book successfully updated";
    }

    @ShellMethod(value = "Add book author. usage: author --bookId 3 --name 'Third Author'", key = {"author"})
    String addAuthor(@ShellOption("--bookId") long id,
                     @ShellOption("--name") String name) {
        bookService.addBookAuthors(id, name);
        return "Author was added successfully";
    }

    @ShellMethod(value = "Delete book by title. usage: d --title 'Book Title'", key = {"d","delete"})
    String deleteBook(String title) {
        bookService.deleteBookByTitle(title);
        return "Book successfully removed from library";
    }
}
