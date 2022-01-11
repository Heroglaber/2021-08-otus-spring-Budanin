package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("BookService should ")
public class BookServiceIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookServiceImpl bookService;

    @Test
    @DisplayName("correctly save book")
    public void shouldAddBook() {
        final String BOOK_TITLE = "The New Title";
        final String FIRST_AUTHOR = "First Author";
        final String SECOND_AUTHOR = "Second Author";
        int authorsListSize = 2;
        BookDTO newBook = new BookDTO(BOOK_TITLE);
        AuthorDTO firstAuthor = new AuthorDTO(FIRST_AUTHOR);
        AuthorDTO secondAuthor = new AuthorDTO(SECOND_AUTHOR);
        newBook.setAuthors(Arrays.asList(firstAuthor, secondAuthor));

        assertThat(newBook.getId()).isBlank();
        assertThat(firstAuthor.getId()).isBlank();
        assertThat(secondAuthor.getId()).isBlank();

        BookDTO inserted = bookService.add(newBook);

        assertThat(inserted.getId()).isNotBlank();
        assertThat(inserted.getAuthors()).allMatch(authorDTO -> !authorDTO.getId().isBlank());
        assertThat(inserted.getAuthors()).extracting(AuthorDTO::getId)
                .allMatch(id -> mongoTemplate.findById(id, Author.class) != null);
        assertThat(inserted.getAuthors()).hasSize(authorsListSize);
        Book actualRecord = mongoTemplate.findById(inserted.getId(), Book.class);
        assertThat(actualRecord.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actualRecord.getAuthors()).allMatch(authorDTO -> !authorDTO.getId().isBlank());
        assertThat(actualRecord.getAuthors()).hasSize(authorsListSize)
                .extracting("name").containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
    }

    @Test
    @DisplayName("correctly save book with authors and genres")
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

        assertThat(newBook.getId()).isBlank();
        assertThat(firstAuthor.getId()).isBlank();
        assertThat(secondAuthor.getId()).isBlank();
        assertThat(firstGenre.getId()).isBlank();
        assertThat(secondGenre.getId()).isBlank();

        BookDTO inserted = bookService.add(newBook);

        assertThat(inserted.getId()).isNotBlank();
        assertThat(inserted.getAuthors()).allMatch(authorDTO -> !authorDTO.getId().isBlank());
        assertThat(inserted.getAuthors()).extracting(AuthorDTO::getId)
                .allMatch(id -> mongoTemplate.findById(id, Author.class) != null);
        assertThat(inserted.getAuthors()).hasSize(authorsListSize);
        assertThat(inserted.getGenres()).allMatch(genreDTO -> !genreDTO.getId().isBlank());
        assertThat(inserted.getGenres()).extracting(GenreDTO::getId)
                .allMatch(id -> mongoTemplate.findById(id, Genre.class) != null);
        assertThat(inserted.getGenres()).hasSize(genresListSize);

        Book actualRecord = mongoTemplate.findById(inserted.getId(), Book.class);
        assertThat(actualRecord.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actualRecord.getAuthors()).hasSize(authorsListSize)
                .extracting("name").containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
        assertThat(actualRecord.getGenres()).hasSize(genresListSize)
                .extracting("name").containsExactlyInAnyOrder(FIRST_GENRE, SECOND_GENRE);
    }

    @Test
    @DisplayName("correctly save book with old author and old genre")
    public void shouldAddBookWithOldAuthorAndGenre() {
        final String BOOK_TITLE = "The New Title";
        String EXISTING_AUTHOR_ID;
        final String EXISTING_AUTHOR = "Stephen King";
        final String SECOND_AUTHOR = "New Genre";
        String EXISTING_GENRE_ID;
        final String EXISTING_GENRE = "detective";
        final String NEW_GENRE = "New Genre";
        final int authorsListSize = 2;

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_AUTHOR));
        Author author = mongoTemplate.find(query, Author.class).get(0);
        EXISTING_AUTHOR_ID = author.getId();

        query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_GENRE));
        Genre genre = mongoTemplate.find(query, Genre.class).get(0);
        EXISTING_GENRE_ID = genre.getId();



        BookDTO newBook = new BookDTO(BOOK_TITLE);
        AuthorDTO firstAuthor = new AuthorDTO(EXISTING_AUTHOR);
        AuthorDTO secondAuthor = new AuthorDTO(SECOND_AUTHOR);
        newBook.setAuthors(Arrays.asList(firstAuthor, secondAuthor));
        GenreDTO firstGenre = new GenreDTO(EXISTING_GENRE);
        GenreDTO secondGenre = new GenreDTO(NEW_GENRE);
        newBook.setGenres(Arrays.asList(firstGenre, secondGenre));


        assertThat(newBook.getId()).isBlank();
        assertThat(firstAuthor.getId()).isBlank();
        assertThat(secondAuthor.getId()).isBlank();
        assertThat(firstGenre.getId()).isBlank();
        assertThat(secondGenre.getId()).isBlank();

        BookDTO inserted = bookService.add(newBook);

        assertThat(inserted.getId()).isNotBlank();
        assertThat(inserted.getAuthors()).allMatch(authorDTO -> !authorDTO.getId().isBlank());
        assertThat(inserted.getAuthors()).extracting(AuthorDTO::getId)
                .allMatch(id -> mongoTemplate.findById(id, Author.class) != null);
        assertThat(inserted.getAuthors()).filteredOn(authorDTO -> authorDTO.getName()
                .equals(EXISTING_AUTHOR))
                .extracting("id").isEqualTo(List.of(EXISTING_AUTHOR_ID));
        assertThat(inserted.getAuthors()).hasSize(authorsListSize);
        assertThat(inserted.getGenres()).allMatch(genreDTO -> !genreDTO.getId().isBlank());
        assertThat(inserted.getGenres()).extracting(GenreDTO::getId)
                .allMatch(id -> mongoTemplate.findById(id, Genre.class) != null);
        assertThat(inserted.getGenres()).filteredOn(genreDTO -> genreDTO.getName()
                .equals(EXISTING_GENRE))
                .extracting("id").isEqualTo(List.of(EXISTING_GENRE_ID));
        Book actualRecord = mongoTemplate.findById(inserted.getId(), Book.class);
        assertThat(actualRecord.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actualRecord.getAuthors()).allMatch(authorDTO -> !authorDTO.getId().isBlank());
        assertThat(actualRecord.getAuthors()).hasSize(authorsListSize)
                .extracting("name")
                .containsExactlyInAnyOrder(EXISTING_AUTHOR, SECOND_AUTHOR);
        assertThat(actualRecord.getAuthors())
                .filteredOn(authorDTO -> authorDTO.getName().equals(EXISTING_AUTHOR))
                .extracting("id").isEqualTo(List.of(EXISTING_AUTHOR_ID));
        assertThat(actualRecord.getGenres()).allMatch(genreDTO -> !genreDTO.getId().isBlank());
        assertThat(actualRecord.getGenres()).extracting(Genre::getId)
                .allMatch(id -> mongoTemplate.findById(id, Genre.class) != null);
        assertThat(actualRecord.getGenres()).filteredOn(g -> g.getName()
                .equals(EXISTING_GENRE))
                .extracting("id").isEqualTo(List.of(EXISTING_GENRE_ID));
    }

    @Test
    @DisplayName("correctly update book title, authors and genres.")
    public void shouldCorrectlyUpdateBook() {
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

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(OLD_BOOK_TITLE));
        Book book = mongoTemplate.find(query, Book.class).get(0);
        String OLD_BOOK_ID = book.getId();

        BookDTO bookBeforeUpdate = bookService.getById(OLD_BOOK_ID);

        assertThat(bookBeforeUpdate.getTitle()).isEqualTo(OLD_BOOK_TITLE);
        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(1)
                .extracting(AuthorDTO::getName).containsExactly(OLD_EXPECTED_AUTHOR);
        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(2)
                .extracting(GenreDTO::getName).containsExactlyInAnyOrder(OLD_EXPECTED_GENRE1, OLD_EXPECTED_GENRE2);


        updatedBook.setId(OLD_BOOK_ID);
        bookService.update(updatedBook);

        BookDTO bookAfterUpdate = bookService.getById(OLD_BOOK_ID);
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
    public void shouldReturnAllBooks() {
        final int EXPECTED_BOOKS_AMOUNT = 10;
        assertThat(bookService.getAll().size()).isEqualTo(EXPECTED_BOOKS_AMOUNT);
        assertThat(bookService.getAll()).
                allMatch(book -> book.getTitle() != null);
    }

    @Test
    @DisplayName("should find book by title")
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
    public void shouldSearchBookById() {
        final String BOOK_TITLE = "Carrie";
        final String EXPECTED_AUTHOR = "Stephen King";
        final String EXPECTED_GENRE1 = "horror";
        final String EXPECTED_GENRE2 = "novel";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

        var book = bookService.getById(BOOK_ID);

        assertThat(book).isNotNull().isInstanceOf(BookDTO.class);
        assertThat(book.getAuthors()).extracting(AuthorDTO::getName)
                .containsExactlyInAnyOrder(EXPECTED_AUTHOR);
        assertThat(book.getGenres()).extracting(GenreDTO::getName)
                .containsExactlyInAnyOrder(EXPECTED_GENRE1, EXPECTED_GENRE2);
    }

    @Test
    @DisplayName("should add author to book")
    public void shouldAddBookAuthor() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        final String NEW_AUTHOR_NAME = "Author Name";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

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
                .allMatch(authorDTO -> !authorDTO.getId().isBlank())
                .extracting(AuthorDTO::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, NEW_AUTHOR_NAME);
        assertThat(mongoTemplate.findById(bookAfterUpdate.getId(), Book.class).getAuthors())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, NEW_AUTHOR_NAME);
        assertThat(mongoTemplate.findById(actualAuthor.getId(), Author.class))
                .extracting("id", "name")
                .containsExactly(actualAuthor.getId(), actualAuthor.getName());
    }

    @Test
    @DisplayName("should add already existing author to book")
    public void shouldAddExistingBookAuthor() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        final String EXISTING_AUTHOR = "Delia Owens";

        //getting id of existing author
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_AUTHOR));
        Author oldAuthor = mongoTemplate.find(query, Author.class).get(0);
        String EXISTING_AUTHOR_ID = oldAuthor.getId();

        //getting id of existing book
        query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

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
                .allMatch(authorDTO -> !authorDTO.getId().isBlank())
                .extracting(AuthorDTO::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, EXISTING_AUTHOR);
        assertThat(mongoTemplate.findById(bookAfterUpdate.getId(), Book.class).getAuthors())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, EXISTING_AUTHOR);
        assertThat(mongoTemplate.findById(actualAuthor.getId(), Author.class))
                .extracting("id", "name")
                .containsExactly(actualAuthor.getId(), actualAuthor.getName());
    }

    @Test
    @DisplayName("should add genre to book")
    public void shouldAddBookGenre() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final String NEW_GENRE = "science non-fiction";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

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
        assertThat(mongoTemplate.findById(bookAfterUpdate.getId(), Book.class).getGenres())
                .extracting(Genre::getName).containsExactlyInAnyOrder(EXPECTED_GENRE, NEW_GENRE);
        assertThat(mongoTemplate.findById(actualGenre.getId(), Genre.class))
                .usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @Test
    @DisplayName("should add already existing genre to book")
    public void shouldAddExistingBookGenre() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final String EXISTING_GENRE = "horror";

        //getting id of existing genre
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_GENRE));
        Genre oldGenre = mongoTemplate.find(query, Genre.class).get(0);
        String EXISTING_GENRE_ID = oldGenre.getId();

        //getting id of existing book
        query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

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
        assertThat(mongoTemplate.findById(bookAfterUpdate.getId(), Book.class).getGenres())
                .extracting(Genre::getName).containsExactlyInAnyOrder(EXPECTED_GENRE, EXISTING_GENRE);
        assertThat(mongoTemplate.findById(actualGenre.getId(), Genre.class))
                .usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @Test
    @DisplayName("should delete author from book")
    public void shouldDeleteBookAuthor() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

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
        assertThat(mongoTemplate.findById(bookAfterUpdate.getId(), Book.class).getAuthors())
                .hasSize(EXPECTED_SIZE - 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR2);
    }

    @Test
    @DisplayName("should throw exception if deleting non-existent book author")
    public void shouldThrowExceptionIfBookAuthorNotExists() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        final String NON_EXISTENT_AUTHOR = "Author not exists";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

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
    public void shouldDeleteBookGenre() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);
        GenreDTO genreRemoved = new GenreDTO(EXPECTED_GENRE);

        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(GenreDTO::getName).containsExactlyInAnyOrder(EXPECTED_GENRE);

        var bookAfterUpdate = bookService.deleteGenre(bookBeforeUpdate, genreRemoved);

        assertThat(bookAfterUpdate.getGenres())
                .isEmpty();
        assertThat(mongoTemplate.findById(bookAfterUpdate.getId(), Book.class).getGenres())
                .isEmpty();
    }

    @Test
    @DisplayName("should throw exception if deleting non-existent book genre")
    public void shouldThrowExceptionIfBookGenreNotExists() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final String NON_EXISTENT_GENRE = "Genre not exitsts";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

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
    void shouldDeleteBookFromLibrary() {
        final String BOOK_TITLE = "The Roadside Picnic";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);

        assertThat(mongoTemplate.findById(bookBeforeUpdate.getId(), Book.class)).isNotNull()
                .extracting(Book::getTitle).isEqualTo(BOOK_TITLE);

        bookService.deleteById(bookBeforeUpdate.getId());

        assertThat(mongoTemplate.findById(bookBeforeUpdate.getId(), Book.class)).isNull();
    }

    @DisplayName("should correctly change book title")
    @Test
    void shouldChangeBookTitle() {
        final String BOOK_TITLE = "Carrie";
        final String NEW_TITLE = "New Title";

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

        BookDTO bookBeforeUpdate = bookService.getById(BOOK_ID);
        assertThat(bookBeforeUpdate.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(mongoTemplate.findById(BOOK_ID, Book.class).getTitle()).isEqualTo(BOOK_TITLE);

        bookService.changeTitle(bookBeforeUpdate, NEW_TITLE);

        BookDTO bookAfterUpdate = bookService.getById(BOOK_ID);
        assertThat(bookAfterUpdate.getTitle()).isEqualTo(NEW_TITLE);
        assertThat(mongoTemplate.findById(BOOK_ID, Book.class).getTitle()).isEqualTo(NEW_TITLE);
    }
}
