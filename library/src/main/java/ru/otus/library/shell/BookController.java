package ru.otus.library.shell;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.CommentDTO;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CommentService commentService;

    @ShellMethod(value = "Save book. usage: i --title 'New Book Title' --genre 'classic' --author 'First Author' --comment 'Great Book!'"
            , key = {"book add", "i", "add", "b a"})
    String addBook(@ShellOption @NotBlank String title,
                      @ShellOption(defaultValue = "") String genre,
                      @ShellOption(defaultValue = "") String author,
                      @ShellOption(defaultValue = "") String comment) {

        BookDTO bookDTO = new BookDTO(title);
        if(!genre.isBlank()) {
            GenreDTO genreDTO = new GenreDTO(genre);
            bookDTO.addGenre(genreDTO);
        }
        if(!author.isBlank()) {
            AuthorDTO authorDTO = new AuthorDTO(author);
            bookDTO.addAuthor(authorDTO);
        }
        bookDTO = bookService.add(bookDTO);

        if(!comment.isBlank()) {
            addComment(bookDTO.getId(), comment);
        }
        return "Book successfully added to library";
    }

    @ShellMethod(value = "Show all books. usage: all", key = {"all", "list", "l"})
    String findAllBooks() throws JsonProcessingException {
        List<BookDTO> books = bookService.getAll();
        return books.toString();
    }

    @ShellMethod(value = "Find a book by title. usage: find --title 'New Book Title'", key = {"find", "f"})
    String findBookByTitle(@ShellOption @NotBlank String title) {
        List<BookDTO> books = bookService.getByTitle(title);
        return books.toString();
    }

    @ShellMethod(value = "Update book. usage: u --id 8 --title 'Updated title' --genre 'Updated genre' --author 'Updated author' --comment 'Updated comment'",
            key = {"book update", "b u", "update", "u"})
    String updateBook(@ShellOption long id,
                      @ShellOption(defaultValue = "") String title,
                      @ShellOption(defaultValue = "") String genre,
                      @ShellOption(defaultValue = "") String author,
                      @ShellOption(defaultValue = "") String comment) {
        BookDTO bookDTO = bookService.getById(id);
        if(!title.isBlank()) {
            bookDTO.setTitle(title);
        }
        if(!genre.isBlank()) {
            GenreDTO genreDTO = new GenreDTO(genre);
            bookDTO.addGenre(genreDTO);
        }
        if(!author.isBlank()) {
            AuthorDTO authorDTO = new AuthorDTO(author);
            bookDTO.addAuthor(authorDTO);
        }
        bookDTO = bookService.update(bookDTO);

        if(!comment.isBlank()) {
            commentService.deleteAllByBookId(bookDTO.getId());
            addComment(bookDTO.getId(), comment);
        }
        return "Book successfully updated";
    }

    @ShellMethod(value = "Add book author. usage: author add --id 8 --name 'Third Author'", key = {"author add", "a a"})
    String addAuthor(@ShellOption("--id") @NotNull long bookId,
                     @ShellOption("--name") @NotBlank String name) {
        BookDTO bookDTO = bookService.getById(bookId);
        AuthorDTO authorDTO = new AuthorDTO(name);
        bookService.addAuthor(bookDTO, authorDTO);
        return "Author was added successfully";
    }

    @ShellMethod(value = "Delete book author. usage: author delete --id 8 --name 'Stephen King'", key = {"author delete", "a d", "a r"})
    String deleteAuthor(@ShellOption("--id") long bookId,
                     @ShellOption("--name") String name) {
        BookDTO bookDTO = bookService.getById(bookId);
        AuthorDTO authorDTO = new AuthorDTO(name);
        bookService.deleteAuthor(bookDTO, authorDTO);
        return "Author was successfully removed";
    }

    @ShellMethod(value = "Add book genre. usage: genre add --id 8 --name 'New genre'", key = {"genre add", "g a"})
    String addGenre(@ShellOption("--id") long bookId,
                     @ShellOption("--name") String name) {
        BookDTO bookDTO = bookService.getById(bookId);
        GenreDTO genreDTO = new GenreDTO(name);
        bookService.addGenre(bookDTO, genreDTO);
        return "Genre was added successfully";
    }

    @ShellMethod(value = "Remove book genre. usage: ", key = {"genre delete", "g d", "g r"})
    String deleteGenre(@ShellOption("--id") long bookId,
                    @ShellOption("--name") String name) {
        BookDTO bookDTO = bookService.getById(bookId);
        GenreDTO genreDTO = new GenreDTO(name);
        bookService.deleteGenre(bookDTO, genreDTO);
        return "Genre was successfully removed";
    }

    @ShellMethod(value = "Add book comment. usage: comment add --id 8 --message 'Great book!'", key = {"comment add", "c a"})
    String addComment(@ShellOption("--id") long bookId,
                    @ShellOption("--message") String message) {
        BookDTO bookDTO = bookService.getById(bookId);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBook(bookDTO);
        commentDTO.setMessage(message);
        commentService.add(commentDTO);
        return "Comment was added successfully";
    }

    @ShellMethod(value = "Remove book comment. usage: comment delete --id 3 --message 'Great book!'", key = {"comment delete", "c d", "c r"})
    String deleteComment(@ShellOption("--id") long bookId,
                      @ShellOption("--message") String message) {
        List<CommentDTO> comments = commentService.getAllByBookId(bookId);
        Optional<CommentDTO> candidate = comments.stream().
                filter(c -> c.getMessage().equals(message)).
                findFirst();
        if(candidate.isPresent()) {
            commentService.delete(candidate.get().getId());
            return "Comment was removed successfully";
        }
        else {
            throw new RuntimeException("Comment not found.");
        }
    }

    @ShellMethod(value = "Show all book comments. usage: comment --id 3", key = {"comment show", "c s", "comment list", "c l"})
    String deleteComment(@ShellOption("--id") long bookId) {
        List<CommentDTO> comments = commentService.getAllByBookId(bookId);
        return comments.toString();
    }

    @ShellMethod(value = "Delete book by id. usage: d 11", key = {"d","delete"})
    String deleteBook(@ShellOption("--id") long id) {
        bookService.deleteById(id);
        return "Book successfully removed from library";
    }
}
