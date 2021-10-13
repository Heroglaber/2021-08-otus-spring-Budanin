package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.domain.Genre;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.GenreDTO;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestEntityManager
@DisplayName("BookService should ")
public class BookServiceIntegrationTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookServiceImpl bookService;

    @Test
    @DisplayName("correctly save book")
    @Transactional
    public void shouldAddBook() {
        final String BOOK_TITLE = "The New Title";
        final String FIRST_AUTHOR = "First Author";
        final String SECOND_AUTHOR = "Second Author";
        int authorsListSize = 2;
        BookDTO newBook = new BookDTO(BOOK_TITLE);
        AuthorDTO firstAuthor = new AuthorDTO(FIRST_AUTHOR);
        AuthorDTO secondAuthor = new AuthorDTO(SECOND_AUTHOR);
        newBook.setAuthors(Arrays.asList(firstAuthor, secondAuthor));

        assertThat(newBook.getId()).isZero();
        assertThat(firstAuthor.getId()).isZero();
        assertThat(secondAuthor.getId()).isZero();

        BookDTO inserted = bookService.add(newBook);

        assertThat(inserted.getId()).isGreaterThan(0);
        assertThat(inserted.getAuthors()).allMatch(authorDTO -> authorDTO.getId() > 0);
        assertThat(inserted.getAuthors()).extracting(AuthorDTO::getId).allMatch(id -> em.find(Author.class, id) != null);
        assertThat(inserted.getAuthors()).hasSize(authorsListSize);
        Book actualRecord = em.find(Book.class, inserted.getId());
        assertThat(actualRecord.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actualRecord.getAuthors()).allMatch(authorDTO -> authorDTO.getId() > 0);
        assertThat(actualRecord.getAuthors()).hasSize(authorsListSize)
                .extracting("name").containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
    }

    @Test
    @DisplayName("correctly save book with authors and genres")
    @Transactional
    public void shouldAddBookWithAuthorsAndGenres() {
        final String BOOK_TITLE = "The New Title";
        final String FIRST_AUTHOR = "First Author";
        final String SECOND_AUTHOR = "Second Author";
        final String FIRST_GENRE = "First Genre";
        final String SECOND_GENRE = "Second Genre";
        int authorsListSize = 2;
        int genresListSize = 2;
        BookDTO newBook = new BookDTO(BOOK_TITLE);
        AuthorDTO firstAuthor = new AuthorDTO(FIRST_AUTHOR);
        AuthorDTO secondAuthor = new AuthorDTO(SECOND_AUTHOR);
        GenreDTO firstGenre = new GenreDTO(FIRST_GENRE);
        GenreDTO secondGenre = new GenreDTO(SECOND_GENRE);
        newBook.setAuthors(Arrays.asList(firstAuthor, secondAuthor));
        newBook.setGenres(Arrays.asList(firstGenre, secondGenre));

        assertThat(newBook.getId()).isZero();
        assertThat(firstAuthor.getId()).isZero();
        assertThat(secondAuthor.getId()).isZero();
        assertThat(firstGenre.getId()).isZero();
        assertThat(secondGenre.getId()).isZero();

        BookDTO inserted = bookService.add(newBook);

        assertThat(inserted.getId()).isGreaterThan(0);
        assertThat(inserted.getAuthors()).allMatch(authorDTO -> authorDTO.getId() > 0);
        assertThat(inserted.getAuthors()).extracting(AuthorDTO::getId).allMatch(id -> em.find(Author.class, id) != null);
        assertThat(inserted.getAuthors()).hasSize(authorsListSize);
        assertThat(inserted.getGenres()).allMatch(genreDTO -> genreDTO.getId() > 0);
        assertThat(inserted.getGenres()).extracting(GenreDTO::getId).allMatch(id -> em.find(Genre.class, id) != null);
        assertThat(inserted.getGenres()).hasSize(genresListSize);

        Book actualRecord = em.find(Book.class, inserted.getId());
        assertThat(actualRecord.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actualRecord.getAuthors()).hasSize(authorsListSize)
                .extracting("name").containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
        assertThat(actualRecord.getGenres()).hasSize(genresListSize)
                .extracting("name").containsExactlyInAnyOrder(FIRST_GENRE, SECOND_GENRE);
    }

    @Test
    @DisplayName("correctly save book with old author and old genre")
    @Transactional
    public void shouldAddBookWithOldAuthorAndGenre() {
        final String BOOK_TITLE = "The New Title";
        final long EXISTED_AUTHOR_ID = 8L;
        final String EXISTED_AUTHOR = "Stephen King";
        final String SECOND_AUTHOR = "New Genre";
        final long EXISTED_GENRE_ID = 2L;
        final String EXISTED_GENRE = "detective";
        final String NEW_GENRE = "New Genre";
        final int authorsListSize = 2;
        BookDTO newBook = new BookDTO(BOOK_TITLE);
        AuthorDTO firstAuthor = new AuthorDTO(EXISTED_AUTHOR);
        AuthorDTO secondAuthor = new AuthorDTO(SECOND_AUTHOR);
        newBook.setAuthors(Arrays.asList(firstAuthor, secondAuthor));
        GenreDTO firstGenre = new GenreDTO(EXISTED_GENRE);
        GenreDTO secondGenre = new GenreDTO(NEW_GENRE);
        newBook.setGenres(Arrays.asList(firstGenre, secondGenre));


        assertThat(newBook.getId()).isZero();
        assertThat(firstAuthor.getId()).isZero();
        assertThat(secondAuthor.getId()).isZero();
        assertThat(firstGenre.getId()).isZero();
        assertThat(secondGenre.getId()).isZero();

        BookDTO inserted = bookService.add(newBook);

        assertThat(inserted.getId()).isGreaterThan(0);
        assertThat(inserted.getAuthors()).allMatch(authorDTO -> authorDTO.getId() > 0);
        assertThat(inserted.getAuthors()).extracting(AuthorDTO::getId).allMatch(id -> em.find(Author.class, id) != null);
        assertThat(inserted.getAuthors()).filteredOn(authorDTO -> authorDTO.getName().equals(EXISTED_AUTHOR))
                .extracting("id").isEqualTo(List.of(EXISTED_AUTHOR_ID));
        assertThat(inserted.getAuthors()).hasSize(authorsListSize);
        assertThat(inserted.getGenres()).allMatch(genreDTO -> genreDTO.getId() > 0);
        assertThat(inserted.getGenres()).extracting(GenreDTO::getId).allMatch(id -> em.find(Genre.class, id) != null);
        assertThat(inserted.getGenres()).filteredOn(genreDTO -> genreDTO.getName().equals(EXISTED_GENRE))
                .extracting("id").isEqualTo(List.of(EXISTED_GENRE_ID));
        Book actualRecord = em.find(Book.class, inserted.getId());
        assertThat(actualRecord.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actualRecord.getAuthors()).allMatch(authorDTO -> authorDTO.getId() > 0);
        assertThat(actualRecord.getAuthors()).hasSize(authorsListSize)
                .extracting("name").containsExactlyInAnyOrder(EXISTED_AUTHOR, SECOND_AUTHOR);
        assertThat(actualRecord.getAuthors()).filteredOn(authorDTO -> authorDTO.getName().equals(EXISTED_AUTHOR))
                .extracting("id").isEqualTo(List.of(EXISTED_AUTHOR_ID));
        assertThat(actualRecord.getGenres()).allMatch(genreDTO -> genreDTO.getId() > 0);
        assertThat(actualRecord.getGenres()).extracting(Genre::getId).allMatch(id -> em.find(Genre.class, id) != null);
        assertThat(actualRecord.getGenres()).filteredOn(genre -> genre.getName().equals(EXISTED_GENRE))
                .extracting("id").isEqualTo(List.of(EXISTED_GENRE_ID));
    }

    @Test
    @DisplayName("correctly update book title, authors and genres.")
    @Transactional
    public void shouldCorrectlyUpdateBook() {
        final long BOOK_ID = 8;
        final String OLD_BOOK_TITLE = "Carrie";
        final String OLD_EXPECTED_AUTHOR = "Stephen King";
        final String OLD_EXPECTED_GENRE1 = "horror";
        final String OLD_EXPECTED_GENRE2 = "novel";
        final String BOOK_TITLE = "The New Title";
        final String FIRST_AUTHOR = "First Author";
        final String SECOND_AUTHOR = "Second Author";
        final String FIRST_GENRE = "First Genre";
        final String SECOND_GENRE = "Second Genre";
        int authorsListSize = 2;
        int genresListSize = 2;
        BookDTO updatedBook = new BookDTO(BOOK_TITLE);
        AuthorDTO firstAuthor = new AuthorDTO(FIRST_AUTHOR);
        AuthorDTO secondAuthor = new AuthorDTO(SECOND_AUTHOR);
        GenreDTO firstGenre = new GenreDTO(FIRST_GENRE);
        GenreDTO secondGenre = new GenreDTO(SECOND_GENRE);
        updatedBook.setAuthors(Arrays.asList(firstAuthor, secondAuthor));
        updatedBook.setGenres(Arrays.asList(firstGenre, secondGenre));
        updatedBook.setId(BOOK_ID);

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);

        assertThat(bookBeforeUpdate.getTitle()).isEqualTo(OLD_BOOK_TITLE);
        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(1)
                .extracting(AuthorDTO::getName).containsExactly(OLD_EXPECTED_AUTHOR);
        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(2)
                .extracting(GenreDTO::getName).containsExactlyInAnyOrder(OLD_EXPECTED_GENRE1, OLD_EXPECTED_GENRE2);


        bookService.update(updatedBook);

        BookDTO bookAfterUpdate = bookService.getById(BOOK_ID);
        assertThat(bookAfterUpdate.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(bookAfterUpdate.getAuthors())
                .hasSize(authorsListSize)
                .extracting(AuthorDTO::getName).containsExactly(FIRST_AUTHOR, SECOND_AUTHOR);
        assertThat(bookAfterUpdate.getGenres())
                .hasSize(genresListSize)
                .extracting(GenreDTO::getName).containsExactlyInAnyOrder(FIRST_GENRE, SECOND_GENRE);

    }

    @Test
    @DisplayName("correctly return all books")
    @Transactional
    public void shouldReturnAllBooks() {
        final int EXPECTED_BOOKS_AMOUNT = 10;
        assertThat(bookService.getAll().size()).isEqualTo(EXPECTED_BOOKS_AMOUNT);
        assertThat(bookService.getAll()).
                allMatch(book -> book.getTitle() != null);
    }

    @Test
    @DisplayName("should find book by title")
    @Transactional
    public void shouldSearchBookByTitle() {
        final String BOOK_TITLE = "Carrie";
        final String EXPECTED_AUTHOR = "Stephen King";
        final String EXPECTED_GENRE1 = "horror";
        final String EXPECTED_GENRE2 = "novel";

        var booksList = bookService.getByTitle(BOOK_TITLE);
        assertThat(booksList).hasSize(1);
        var book = booksList.get(0);

        assertThat(book).isNotNull().isInstanceOf(BookDTO.class);
        assertThat(book.getAuthors()).extracting(AuthorDTO::getName)
                .containsExactlyInAnyOrder(EXPECTED_AUTHOR);
        assertThat(book.getGenres()).extracting(GenreDTO::getName)
                .containsExactlyInAnyOrder(EXPECTED_GENRE1, EXPECTED_GENRE2);
    }

    @Test
    @DisplayName("should find book by id")
    @Transactional
    public void shouldSearchBookById() {
        final long BOOK_ID = 8;
        final String EXPECTED_AUTHOR = "Stephen King";
        final String EXPECTED_GENRE1 = "horror";
        final String EXPECTED_GENRE2 = "novel";

        var book = bookService.getById(BOOK_ID);

        assertThat(book).isNotNull().isInstanceOf(BookDTO.class);
        assertThat(book.getAuthors()).extracting(AuthorDTO::getName)
                .containsExactlyInAnyOrder(EXPECTED_AUTHOR);
        assertThat(book.getGenres()).extracting(GenreDTO::getName)
                .containsExactlyInAnyOrder(EXPECTED_GENRE1, EXPECTED_GENRE2);
    }

    @Test
    @DisplayName("should add author to book")
    @Transactional
    public void shouldAddBookAuthor() {
        final long BOOK_ID = 1;
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        final String NEW_AUTHOR_NAME = "Author Name";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);

        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(AuthorDTO::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        AuthorDTO newAuthor = new AuthorDTO(NEW_AUTHOR_NAME);

        var bookAfterUpdate = bookService.addAuthor(bookBeforeUpdate, newAuthor);
        AuthorDTO actualAuthor = bookAfterUpdate.getAuthors().stream()
                .filter(a -> a.getName().equals(NEW_AUTHOR_NAME)).findFirst().get();

        assertThat(bookAfterUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE + 1)
                .allMatch(authorDTO -> authorDTO.getId() > 0)
                .extracting(AuthorDTO::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, NEW_AUTHOR_NAME);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getAuthors())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, NEW_AUTHOR_NAME);
        assertThat(em.find(Author.class, actualAuthor.getId()))
                .usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualAuthor);
    }

    @Test
    @DisplayName("should add already existing author to book")
    @Transactional
    public void shouldAddExistingBookAuthor() {
        final long BOOK_ID = 1;
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        final long EXISTING_AUTHOR_ID = 6L;
        final String EXISTING_AUTHOR = "Delia Owens";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);

        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(AuthorDTO::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        AuthorDTO newAuthor = new AuthorDTO(EXISTING_AUTHOR);

        var bookAfterUpdate = bookService.addAuthor(bookBeforeUpdate, newAuthor);
        AuthorDTO actualAuthor = bookAfterUpdate.getAuthors().stream()
                .filter(a -> a.getName().equals(EXISTING_AUTHOR)).findFirst().get();
        assertThat(actualAuthor.getId()).isEqualTo(EXISTING_AUTHOR_ID);

        assertThat(bookAfterUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE + 1)
                .allMatch(authorDTO -> authorDTO.getId() > 0)
                .extracting(AuthorDTO::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, EXISTING_AUTHOR);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getAuthors())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, EXISTING_AUTHOR);
        assertThat(em.find(Author.class, actualAuthor.getId()))
                .usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualAuthor);
    }

    @Test
    @DisplayName("should add genre to book")
    @Transactional
    public void shouldAddBookGenre() {
        final long BOOK_ID = 1;
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final String NEW_GENRE = "science non-fiction";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);

        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(GenreDTO::getName).containsExactly(EXPECTED_GENRE);

        GenreDTO newGenre = new GenreDTO(NEW_GENRE);

        var bookAfterUpdate = bookService.addGenre(bookBeforeUpdate, newGenre);
        GenreDTO actualGenre = bookAfterUpdate.getGenres().stream()
                .filter(a -> a.getName().equals(NEW_GENRE)).findFirst().get();

        assertThat(bookAfterUpdate.getGenres())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(GenreDTO::getName).containsExactly(EXPECTED_GENRE, NEW_GENRE);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getGenres())
                .extracting(Genre::getName).containsExactlyInAnyOrder(EXPECTED_GENRE, NEW_GENRE);
        assertThat(em.find(Genre.class, actualGenre.getId()))
                .usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @Test
    @DisplayName("should add  already existing genre to book")
    @Transactional
    public void shouldAddExistingBookGenre() {
        final long BOOK_ID = 1;
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final long EXISTING_GENRE_ID = 3L;
        final String EXISTING_GENRE = "horror";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);

        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(GenreDTO::getName).containsExactly(EXPECTED_GENRE);

        GenreDTO newGenre = new GenreDTO(EXISTING_GENRE);

        var bookAfterUpdate = bookService.addGenre(bookBeforeUpdate, newGenre);
        GenreDTO actualGenre = bookAfterUpdate.getGenres().stream()
                .filter(a -> a.getName().equals(EXISTING_GENRE)).findFirst().get();
        assertThat(actualGenre.getId()).isEqualTo(EXISTING_GENRE_ID);

        assertThat(bookAfterUpdate.getGenres())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(GenreDTO::getName).containsExactly(EXPECTED_GENRE, EXISTING_GENRE);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getGenres())
                .extracting(Genre::getName).containsExactlyInAnyOrder(EXPECTED_GENRE, EXISTING_GENRE);
        assertThat(em.find(Genre.class, actualGenre.getId()))
                .usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @Test
    @DisplayName("should delete author from book")
    @Transactional
    public void shouldDeleteBookAuthor() {
        final long BOOK_ID = 1;
        //final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);
        AuthorDTO authorRemoved = new AuthorDTO(EXPECTED_AUTHOR1);

        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(AuthorDTO::getName).containsExactlyInAnyOrder(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        var bookAfterUpdate = bookService.deleteAuthor(bookBeforeUpdate, authorRemoved);

        assertThat(bookAfterUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE - 1)
                .extracting(AuthorDTO::getName).containsExactly(EXPECTED_AUTHOR2);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getAuthors())
                .hasSize(EXPECTED_SIZE - 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR2);
    }

    @Test
    @DisplayName("should throw exception if deleting non-existent book author")
    @Transactional
    public void shouldThrowExceptionIfBookAuthorNotExists() {
        final long BOOK_ID = 1;
        //final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        final String NON_EXISTENT_AUTHOR = "Author not exists";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);
        AuthorDTO authorRemoved = new AuthorDTO(NON_EXISTENT_AUTHOR);

        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(AuthorDTO::getName).containsExactlyInAnyOrder(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            var bookAfterUpdate = bookService.deleteAuthor(bookBeforeUpdate, authorRemoved);
        });
        assertEquals("The book does not have such an author.", exception.getMessage());
    }

    @Test
    @DisplayName("should delete book genre")
    @Transactional
    public void shouldDeleteBookGenre() {
        final long BOOK_ID = 1;
        //final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);
        GenreDTO genreRemoved = new GenreDTO(EXPECTED_GENRE);

        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(GenreDTO::getName).containsExactlyInAnyOrder(EXPECTED_GENRE);

        var bookAfterUpdate = bookService.deleteGenre(bookBeforeUpdate, genreRemoved);

        assertThat(bookAfterUpdate.getGenres())
                .isEmpty();
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getGenres())
                .isEmpty();
    }

    @Test
    @DisplayName("should throw exception if deleting non-existent book genre")
    @Transactional
    public void shouldThrowExceptionIfBookGenreNotExists() {
        final long BOOK_ID = 1;
        //final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final String NON_EXISTENT_GENRE = "Genre not exitsts";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);
        GenreDTO genreRemoved = new GenreDTO(NON_EXISTENT_GENRE);

        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(GenreDTO::getName).containsExactlyInAnyOrder(EXPECTED_GENRE);

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            var bookAfterUpdate = bookService.deleteGenre(bookBeforeUpdate, genreRemoved);
        });
        assertEquals("The book does not have such a genre.", exception.getMessage());
    }

    @DisplayName("should delete book by id")
    @Test
    @Transactional
    void shouldDeleteBookFromLibrary() {
        final long BOOK_ID = 1;
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);

        assertThat(em.find(Book.class, bookBeforeUpdate.getId())).isNotNull()
                .extracting(Book::getTitle).isEqualTo(BOOK_TITLE);

        bookService.deleteById(bookBeforeUpdate.getId());

        assertThat(em.find(Book.class, bookBeforeUpdate.getId())).isNull();
    }

    @DisplayName("should correctly change book title")
    @Test
    @Transactional
    void shouldChangeBookTitle() {
        final long BOOK_ID = 8;
        final String BOOK_TITLE = "Carrie";
        final String NEW_TITLE = "New Title";

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);
        assertThat(bookBeforeUpdate.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(em.find(Book.class, BOOK_ID).getTitle()).isEqualTo(BOOK_TITLE);

        bookService.changeTitle(bookBeforeUpdate, NEW_TITLE);

        BookDTO bookAfterUpdate = bookService.getById(BOOK_ID);
        assertThat(bookAfterUpdate.getTitle()).isEqualTo(NEW_TITLE);
        assertThat(em.find(Book.class, BOOK_ID).getTitle()).isEqualTo(NEW_TITLE);
    }
}
