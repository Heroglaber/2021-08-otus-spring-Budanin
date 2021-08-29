package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.BookDTO;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookService must ")
@SpringBootTest
public class BookServiceIntegrationTest {
    private static final String AUTHOR_NAME = "Arkady Strugatsky";
    private static final String FANTASY_GENRE = "fantasy";
    private static final String CLASSIC_GENRE = "classic";
    private static final String BOOK_TITLE = "The Final Circle of Paradise";
    private static final String NEW_BOOK_TITLE = "The War and Peace";
    private static final String ADVENTURE_BOOK = "The Three Musketeers";
    private static final String ADVENTURE_GENRE = "adventure";
    private static final String ADVENTURE_AUTHOR = "Alexandre Dumas";

    @Autowired
    private BookService bookService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private AuthorService authorService;

    @DisplayName("find a book by title")
    @Test
    void shouldFindBookByTitle() {
        BookDTO book = bookService.findByTitle(BOOK_TITLE);
        assertThat(book)
                .isEqualTo(new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                        List.of("Arkady Strugatsky", "Boris Strugatsky")));
    }

    @DisplayName("find a book by id")
    @Test
    void shouldFindBookById() {
        BookDTO book = bookService.findById(2);
        assertThat(book)
                .isEqualTo(new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                        List.of("Arkady Strugatsky", "Boris Strugatsky")));
    }

    @DisplayName("successfully add new book")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddBook() {
        BookDTO newBook = new BookDTO(3, NEW_BOOK_TITLE, CLASSIC_GENRE);
        //before adding
        assertThat(bookService.findByTitle(NEW_BOOK_TITLE))
                .isNull();
        bookService.addBook(newBook);
        //after adding
        assertThat(bookService.findByTitle(NEW_BOOK_TITLE))
                .isEqualTo(new BookDTO(3, "The War and Peace", "classic", Collections.emptyList()));
    }

    @DisplayName("successfully add new book with new genre")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddBookWithNewGenre() {
        BookDTO newBook = new BookDTO(3, ADVENTURE_BOOK, ADVENTURE_GENRE);
        //before adding
        assertThat(bookService.findByTitle(NEW_BOOK_TITLE))
                .isNull();
        assertThat(genreService.findByName(ADVENTURE_GENRE))
                .isNull();
        bookService.addBook(newBook);
        //after adding
        assertThat(bookService.findByTitle(ADVENTURE_BOOK))
                .isEqualTo(new BookDTO(3, "The Three Musketeers", "adventure", Collections.emptyList()));
        assertThat(genreService.findByName(ADVENTURE_GENRE))
                .isInstanceOf(Genre.class);
    }

    @DisplayName("successfully delete book by title")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteBookByTitle() {
        BookDTO book = bookService.findByTitle(BOOK_TITLE);
        assertThat(book)
                .isEqualTo(new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                        List.of("Arkady Strugatsky", "Boris Strugatsky")));

        bookService.deleteBookByTitle(BOOK_TITLE);

        assertThat(bookService.findByTitle(BOOK_TITLE))
                .isNull();
    }

    @DisplayName("successfully add new book with new genre and new author")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddBookWithNewGenreAndAuthor() {
        BookDTO newBook = new BookDTO(3, ADVENTURE_BOOK, ADVENTURE_GENRE, List.of(ADVENTURE_AUTHOR));
        //before adding
        assertThat(bookService.findByTitle(NEW_BOOK_TITLE))
                .isNull();
        assertThat(genreService.findByName(ADVENTURE_GENRE))
                .isNull();
        assertThat(authorService.findByName(ADVENTURE_AUTHOR))
                .isNull();

        bookService.addBook(newBook);

        //after adding
        assertThat(bookService.findByTitle(ADVENTURE_BOOK))
                .isEqualTo(new BookDTO(3, "The Three Musketeers", "adventure", List.of("Alexandre Dumas")));
        assertThat(genreService.findByName(ADVENTURE_GENRE))
                .isInstanceOf(Genre.class);
        assertThat(authorService.findByName(ADVENTURE_AUTHOR))
                .isInstanceOf(Author.class);
    }

    @DisplayName("successfully update book")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingBook() {
        BookDTO book = bookService.findById(2);
        assertThat(book)
                .isEqualTo(new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                        List.of("Arkady Strugatsky", "Boris Strugatsky")));

        BookDTO updatedBook = new BookDTO(book.getId(), "New Title", "classic");
        updatedBook.setAuthors(List.of("Lev Tolstoy", "Fedor Dostoevsky"));
        bookService.updateBook(updatedBook);

        book = bookService.findById(2);
        assertThat(book)
                .isEqualTo(new BookDTO(2, "New Title", "classic",
                        List.of("Lev Tolstoy", "Fedor Dostoevsky")));
    }

    @DisplayName("add book author")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddBookAuthor() {
        BookDTO book = bookService.findById(2);
        assertThat(book)
                .isEqualTo(new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                        List.of("Arkady Strugatsky", "Boris Strugatsky")));

        bookService.addBookAuthors(2, "Lev Tolstoy", "Fedor Dostoevsky");

        assertThat(bookService.findById(2))
                .isEqualTo(new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                        List.of("Arkady Strugatsky",
                                "Boris Strugatsky",
                                "Lev Tolstoy",
                                "Fedor Dostoevsky")));
    }

    @DisplayName("return the expected number of books")
    @Test
    void shouldReturnExpectedBooks() {
        List<BookDTO> books = bookService.getAll();
        assertThat(books)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new BookDTO(1, "The Roadside Picnic", "fantasy",
                                List.of("Arkady Strugatsky", "Boris Strugatsky")),
                        new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                                List.of("Arkady Strugatsky", "Boris Strugatsky")));
    }

    @DisplayName("return all of the author's books")
    @Test
    void shouldReturnAllAuthorBooks() {
        List<BookDTO> books = bookService.getAllByAuthor(AUTHOR_NAME);
        assertThat(books)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new BookDTO(1, "The Roadside Picnic", "fantasy",
                                List.of("Arkady Strugatsky", "Boris Strugatsky")),
                        new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                                List.of("Arkady Strugatsky", "Boris Strugatsky")));
    }

    @DisplayName("return all books of the given genre")
    @Test
    void shouldReturnAllBooksByGenre() {
        List<BookDTO> books = bookService.getAllByGenre(FANTASY_GENRE);
        assertThat(books)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new BookDTO(1, "The Roadside Picnic", "fantasy",
                                List.of("Arkady Strugatsky", "Boris Strugatsky")),
                        new BookDTO(2, "The Final Circle of Paradise", "fantasy",
                                List.of("Arkady Strugatsky", "Boris Strugatsky")));
    }
}
