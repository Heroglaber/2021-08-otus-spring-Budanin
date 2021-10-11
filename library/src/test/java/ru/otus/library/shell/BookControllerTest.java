package ru.otus.library.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.otus.library.shell.utils.InputWithCommandArguments;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Shell commands tests")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BookControllerTest {
    @Autowired
    private Shell shell;

    private static final String COMMAND_LIST = "list";
    private static final int BOOKS_LIST_SIZE = 10;
    private static final String COMMAND_FIND_NON_EXISTENT = "find --title 'New Book Title'";
    private static final String COMMAND_FIND = "find --title 'Carrie'";
    private static final String FIND_RESULT = "[BookDTO(id=8, title=Carrie, authors=[AuthorDTO(id=8, name=Stephen King)], genres=[GenreDTO(id=3, name=horror), GenreDTO(id=5, name=novel)])]";
    private static final String COMMAND_SAVE = "i --title 'New Book Title' --genre 'classic' --author 'First Author' --comment 'Great Book!'";
    private static final String SUCCESSFULLY_SAVED = "Book successfully added to library";
    private static final String SAVED_BOOK = "BookDto(id=11, title=New Book Title, comments=[Comment(id=6, book=Book(id=11, title=New Book Title, authors=[Author(id=12, name=First Author)], genres=[Genre(id=8, name=classic)]), message=Great Book!)], authors=[Author(id=12, name=First Author)], genres=[Genre(id=8, name=classic)])";
    private static final String COMMAND_UPDATE = "u --id 8 --title 'Updated title' --genre 'Updated genre' --author 'Updated author' --comment 'Updated comment'";
    private static final String SUCCESSFULLY_UPDATED = "Book successfully updated";
    private static final String COMMAND_FIND_UPDATED = "find --title 'Updated title'";
    private static final String UPDATED_BOOK = "[BookDTO(id=8, title=Updated title, authors=[AuthorDTO(id=8, name=Stephen King), AuthorDTO(id=11, name=Updated author)], genres=[GenreDTO(id=3, name=horror), GenreDTO(id=5, name=novel), GenreDTO(id=7, name=Updated genre)])]";
    private static final String COMMAND_ADD_AUTHOR = "author add --id 8 --name 'New Author'";
    private static final String SUCCESSFULLY_ADD_AUTHOR = "Author was added successfully";
    private static final String BOOK_AFTER_ADD_AUTHOR = "[BookDTO(id=8, title=Carrie, authors=[AuthorDTO(id=8, name=Stephen King), AuthorDTO(id=13, name=New Author)], genres=[GenreDTO(id=3, name=horror), GenreDTO(id=5, name=novel)])]";
    
    @DisplayName(" should prints all books in library")
    @Test
    @Transactional
    void shouldPrintsAllBooks() {
        String res = (String) shell.evaluate(() -> COMMAND_LIST);
        assertThat(StringUtils.countOccurrencesOf(res, "BookDTO")).isEqualTo(BOOKS_LIST_SIZE);
    }

    @DisplayName(" should find book by title")
    @Test
    @Transactional
    void shouldFindBookByTitle(){
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(StringUtils.countOccurrencesOf(res, "BookDTO")).isEqualTo(1);
        assertThat(StringUtils.countOccurrencesOf(res, "BookDTO(id=8")).isEqualTo(1);
    }

    @DisplayName(" should throw NoSuchElementException if book not found")
    @Test
    @Transactional
    void shouldThrowExceptionIfBookNotFound(){
        Throwable throwable = (Throwable) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(throwable).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName(" should correctly save book in library")
    @Test
    @Transactional
    void shouldCorrectlySaveBook() {
        Throwable throwable = (Throwable) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(throwable).isInstanceOf(NoSuchElementException.class);

        String result = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_SAVE));
        assertThat(result).isEqualTo(SUCCESSFULLY_SAVED);

        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(StringUtils.countOccurrencesOf((String)res, "BookDTO")).isEqualTo(1);
        assertThat(StringUtils.countOccurrencesOf((String)res, "BookDTO(id=11")).isEqualTo(1);
        assertThat(StringUtils.countOccurrencesOf((String)res, "First Author")).isEqualTo(1);
        assertThat(StringUtils.countOccurrencesOf((String)res, "classic")).isEqualTo(1);
    }

    @DisplayName(" should correctly update book in library")
    @Test
    @Transactional
    void shouldCorrectlyUpdateBook() {
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res).isEqualTo(String.format(FIND_RESULT));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_UPDATE));
        assertThat(res).isEqualTo(String.format(SUCCESSFULLY_UPDATED));

        Throwable throwable = (Throwable) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(throwable).isInstanceOf(NoSuchElementException.class);

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_UPDATED));
        assertThat(res).isEqualTo(String.format(UPDATED_BOOK));
    }

    @DisplayName(" should correctly add author to book")
    @Test
    @Transactional
    void shouldCorrectlyAddAuthor() {
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res).isEqualTo(String.format(FIND_RESULT));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_ADD_AUTHOR));
        assertThat(res).isEqualTo(String.format(SUCCESSFULLY_ADD_AUTHOR));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res).isEqualTo(String.format(BOOK_AFTER_ADD_AUTHOR));
    }
}
