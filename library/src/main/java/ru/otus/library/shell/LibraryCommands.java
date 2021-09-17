package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.dto.BookDto;
import ru.otus.library.models.Author;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;
import ru.otus.library.services.BookService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class LibraryCommands {
    private final BookService bookService;

    @ShellMethod(value = "Save book. usage: i --title 'New Book Title' --genre 'classic' --author 'First Author' --comment 'Great Book!'"
            , key = {"insert", "i"})
    String insertBook(@ShellOption String title,
                      @ShellOption(defaultValue = "") String genre,
                      @ShellOption(defaultValue = "") String author,
                      @ShellOption(defaultValue = "") String comment) {

        BookDto book = new BookDto();
        book.setTitle(title);
        if(comment != null && !comment.isEmpty()) {
            book.setComments(List.of(new Comment(comment)));
        }
        if(genre != null && !genre.isEmpty()) {
            book.setGenres(List.of(new Genre(genre)));
        }
        if(author != null && !author.isEmpty()) {
            book.setAuthors(List.of(new Author(author)));
        }
        bookService.addBook(book);
        return "Book successfully added to library";
    }

    @ShellMethod(value = "Show all books. usage: all", key = {"all", "list", "l"})
    String findAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return books.toString();
    }

    @ShellMethod(value = "Find a book by title. usage: find --title 'New Book Title'", key = {"find", "f"})
    String findBookByTitle(@ShellOption String title) {
        BookDto book = bookService.findBookByTitle(title);
        return book.toString();
    }

    @ShellMethod(value = "Update book. usage: u --id 11 --title 'Book Title' --genre 'fantasy' --author 'Second Author'", key = {"update","u"})
    String updateBook(@ShellOption long id,
                      @ShellOption(defaultValue = "") String title,
                      @ShellOption(defaultValue = "") String genre,
                      @ShellOption(defaultValue = "") String author,
                      @ShellOption(defaultValue = "") String comment) {
        BookDto book = bookService.findBookById(id);
        if(title != null && !title.isEmpty())
            {bookService.changeBookTitle(book, title);}
        if(comment != null && !comment.isEmpty())
            {bookService.addBookComment(book, new Comment(comment));}
        if(genre != null && !genre.isEmpty())
            {bookService.addBookGenre(book, new Genre(genre));}
        if(author != null && !author.isEmpty())
            {bookService.addBookAuthor(book, new Author(author));}
        return "Book successfully updated";
    }

    @ShellMethod(value = "Add or delete book author. usage: author --delete true --id 3 --name 'Third Author'", key = {"author"})
    String addAuthor(@ShellOption(value = "--delete", defaultValue = "false") String delete,
                     @ShellOption("--id") long bookId,
                     @ShellOption("--name") String name) {
        BookDto book = bookService.findBookById(bookId);
        if(delete.equals("false")) {
            bookService.addBookAuthor(book, new Author(name));
            return "Author was added successfully";
        }
        else {
            bookService.deleteBookAuthor(book, new Author(name));
            return "Author was successfully removed";
        }
    }

    @ShellMethod(value = "Add or remove book genre. usage: genre --delete true --id 3 --name 'New genre'", key = {"genre"})
    String addGenre(@ShellOption(value = "--delete", defaultValue = "false") String delete,
                     @ShellOption("--id") long bookId,
                     @ShellOption("--name") String genre) {
        BookDto book = bookService.findBookById(bookId);
        if(delete.equals("false")) {
            bookService.addBookGenre(book, new Genre(genre));
            return "Genre was added successfully";
        }
        else {
            bookService.deleteBookGenre(book, new Genre(genre));
            return "Genre was successfully removed";
        }
    }

    @ShellMethod(value = "Add or remove book comment. usage: comment --id 3 --name 'Great book!'", key = {"comment"})
    String addComment(@ShellOption(value = "--delete", defaultValue = "false") String delete,
                    @ShellOption("--id") long bookId,
                    @ShellOption("--name") String comment) {
        BookDto book = bookService.findBookById(bookId);
        if(delete.equals("false")) {
            bookService.addBookComment(book, new Comment(comment));
            return "Comment was added successfully";
        }
        else {
            bookService.deleteBookComment(book, new Comment(comment));
            return "Comment was successfully removed";
        }
    }

    @ShellMethod(value = "Delete book by title. usage: d --title 'Book Title'", key = {"d","delete"})
    String deleteBook(String title) {
        BookDto book = bookService.findBookByTitle(title);
        bookService.deleteBookById(book.getId());
        return "Book successfully removed from library";
    }
}
