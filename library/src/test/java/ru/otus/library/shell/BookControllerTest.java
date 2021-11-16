package ru.otus.library.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.otus.library.models.domain.Book;
import ru.otus.library.shell.utils.InputWithCommandArguments;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Shell commands tests")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookControllerTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Shell shell;

    private static final String COMMAND_LIST = "list";
    private static final int BOOKS_LIST_SIZE = 10;
    private static final String EXISTING_BOOK_TITLE = "Carrie";
    private static final String COMMAND_FIND_NON_EXISTENT = "find --title 'New Book Title'";
    private static final String COMMAND_FIND = "find --title 'Carrie'";
    private static final String FIND_RESULT = "\\[BookDTO\\(id=.+, title=Carrie, authors=\\[AuthorDTO\\(id=.+, name=Stephen King, books=null\\)\\], genres=\\[GenreDTO\\(id=.+, name=horror\\), GenreDTO\\(id=.+, name=novel\\)\\]\\)\\]";
    private static final String COMMAND_SAVE = "i --title 'New Book Title' --genre 'classic' --author 'First Author' --comment 'Great Book!'";
    private static final String SUCCESSFULLY_SAVED = "Book successfully added to library";
    private static final String SAVED_BOOK = "\\[BookDTO\\(id=.+, title=New Book Title, authors=\\[AuthorDTO\\(id=.+, name=First Author, books=null\\)\\], genres=\\[GenreDTO\\(id=.+, name=classic\\)\\]\\)\\]";
    private static final String COMMAND_UPDATE = "u --id * --title 'Updated title' --genre 'Updated genre' --author 'Updated author' --comment 'Updated comment'";
    private static final String SUCCESSFULLY_UPDATED = "Book successfully updated";
    private static final String COMMAND_FIND_UPDATED = "find --title 'Updated title'";
    private static final String UPDATED_BOOK = "\\[BookDTO\\(id=.+, title=Updated title, authors=\\[AuthorDTO\\(id=.+, name=Stephen King, books=null\\), AuthorDTO\\(id=.+, name=Updated author, books=null\\)\\], genres=\\[GenreDTO\\(id=.+, name=horror\\), GenreDTO\\(id=.+, name=novel\\), GenreDTO\\(id=.+, name=Updated genre\\)\\]\\)\\]";
    private static final String COMMAND_ADD_AUTHOR = "author add --id * --name 'New Author'";
    private static final String SUCCESSFULLY_ADD_AUTHOR = "Author was added successfully";
    private static final String BOOK_AFTER_ADD_AUTHOR = "\\[BookDTO\\(id=.+, title=Carrie, authors=\\[AuthorDTO\\(id=.+, name=Stephen King, books=null\\), AuthorDTO\\(id=.+, name=New Author, books=null\\)\\], genres=\\[GenreDTO\\(id=.+, name=horror\\), GenreDTO\\(id=.+, name=novel\\)\\]\\)\\]";
    
    @DisplayName(" should prints all books in library")
    @Test
    void shouldPrintsAllBooks() {
        String res = (String) shell.evaluate(() -> COMMAND_LIST);
        assertThat(StringUtils.countOccurrencesOf(res, "BookDTO")).isEqualTo(BOOKS_LIST_SIZE);
    }

    @DisplayName(" should find book by title")
    @Test
    void shouldFindBookByTitle(){
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res.matches(FIND_RESULT));
    }

    @DisplayName(" should correctly save book in library")
    @Test
    void shouldCorrectlySaveBook() {
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(res).isEqualTo("[]");

        String result = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_SAVE));
        assertThat(result).isEqualTo(SUCCESSFULLY_SAVED);

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(res.matches(SAVED_BOOK));
    }

    @DisplayName(" should correctly update book in library")
    @Test
    void shouldCorrectlyUpdateBook() {
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res.matches(FIND_RESULT));

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(EXISTING_BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_UPDATE.replace("*", BOOK_ID)));
        assertThat(res).isEqualTo(String.format(SUCCESSFULLY_UPDATED));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res).isEqualTo("[]");

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_UPDATED));
        assertThat(res.matches(UPDATED_BOOK));
    }

    @DisplayName(" should correctly add author to book")
    @Test
    void shouldCorrectlyAddAuthor() {
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res.matches(FIND_RESULT));

        //getting id of existing book
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(EXISTING_BOOK_TITLE));
        Book oldBook = mongoTemplate.find(query, Book.class).get(0);
        String BOOK_ID = oldBook.getId();

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_ADD_AUTHOR.replace("*", BOOK_ID)));
        assertThat(res).isEqualTo(String.format(SUCCESSFULLY_ADD_AUTHOR));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res.matches(BOOK_AFTER_ADD_AUTHOR));
    }
}
