package ru.otus.library.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Genre;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.CommentDTO;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.repositories.AuthorRepository;
import ru.otus.library.repositories.GenreRepository;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.Arrays;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "abudanin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "abudanin")
    public void insertGenres(GenreRepository repository) {
        repository.save(new Genre("classic"));
        repository.save(new Genre("detective"));
        repository.save(new Genre("horror"));
        repository.save(new Genre("fantasy"));
        repository.save(new Genre("novel"));
        repository.save(new Genre("unknown"));
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "abudanin")
    public void insertAuthors(AuthorRepository repository) {
        repository.save(new Author("Arkady Strugatsky"));
        repository.save(new Author("Boris Strugatsky"));
        repository.save(new Author("Gabriel Garcia Marquez"));
        repository.save(new Author("Louisa May Alcott"));
        repository.save(new Author("Toni Morrison"));
        repository.save(new Author("Delia Owens"));
        repository.save(new Author("Jeannette Walls"));
        repository.save(new Author("Stephen King"));
        repository.save(new Author("Trevor Noah"));
        repository.save(new Author("Josh Malerman"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "abudanin")
    public void insertBooks(BookService bookService) {
        bookService.add(toBookDTO("The Roadside Picnic",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy")));

        bookService.add(toBookDTO("The Final Circle of Paradise",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy")));

        bookService.add(toBookDTO("One Hundred Years of Solitude",
                Arrays.asList("Gabriel Garcia Marquez"),
                Arrays.asList("novel")));

        bookService.add(toBookDTO("Little Women",
                Arrays.asList("Louisa May Alcott"),
                Arrays.asList("novel")));

        bookService.add(toBookDTO("Beloved",
                Arrays.asList("Toni Morrison"),
                Arrays.asList("novel")));

        bookService.add(toBookDTO("Where the Crawdads Sing",
                Arrays.asList("Delia Owens"),
                Arrays.asList("novel")));

        bookService.add(toBookDTO("The Glass Castle",
                Arrays.asList("Jeannette Walls"),
                Arrays.asList("unknown")));

        bookService.add(toBookDTO("Carrie",
                Arrays.asList("Stephen King"),
                Arrays.asList("horror", "novel")));

        bookService.add(toBookDTO("Born a Crime",
                Arrays.asList("Trevor Noah"),
                Arrays.asList("unknown")));

        bookService.add(toBookDTO("Bird Box",
                Arrays.asList("Josh Malerman"),
                Arrays.asList("novel")));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "abudanin")
    public void insertComments(CommentService commentService, BookService bookService) {
        commentService.add(new CommentDTO(bookService.getByTitle("The Roadside Picnic").get(0)
                , "Greatest book ever!"));
        commentService.add(new CommentDTO(bookService.getByTitle("The Roadside Picnic").get(0)
                , "I read this book as a child."));
        commentService.add(new CommentDTO(bookService.getByTitle("Carrie").get(0)
                , "Scary book!"));
        commentService.add(new CommentDTO(bookService.getByTitle("Carrie").get(0)
                , "JUST AMAZING"));
    }

    private BookDTO toBookDTO(String title, List<String> authors, List<String> genres) {
        var book = new BookDTO(title);
        for(String author : authors) {
            book.addAuthor(new AuthorDTO(author));
        }
        for(String genre: genres) {
            book.addGenre(new GenreDTO(genre));
        }
        return book;
    }
}
