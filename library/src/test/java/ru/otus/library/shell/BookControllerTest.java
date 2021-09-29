package ru.otus.library.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.shell.utils.InputWithCommandArguments;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Shell commands tests")
@SpringBootTest
public class BookControllerTest {

    @Autowired
    private Shell shell;

    private static final String COMMAND_LIST = "list";
    private static final String BOOKS_LIST = "[BookDto(id=1, title=The Roadside Picnic, comments=[Comment(id=1, book=Book(id=1, title=The Roadside Picnic, authors=[Author(id=1, name=Arkady Strugatsky), Author(id=2, name=Boris Strugatsky)], genres=[Genre(id=4, name=fantasy)]), message=Greatest book ever!), Comment(id=2, book=Book(id=1, title=The Roadside Picnic, authors=[Author(id=1, name=Arkady Strugatsky), Author(id=2, name=Boris Strugatsky)], genres=[Genre(id=4, name=fantasy)]), message=I read this book as a child.)], authors=[Author(id=1, name=Arkady Strugatsky), Author(id=2, name=Boris Strugatsky)], genres=[Genre(id=4, name=fantasy)]), BookDto(id=2, title=The Final Circle of Paradise, comments=[], authors=[Author(id=1, name=Arkady Strugatsky), Author(id=2, name=Boris Strugatsky)], genres=[Genre(id=4, name=fantasy)]), BookDto(id=3, title=One Hundred Years of Solitude, comments=[], authors=[Author(id=3, name=Gabriel Garcia Marquez)], genres=[Genre(id=5, name=novel)]), BookDto(id=4, title=Little Women, comments=[], authors=[Author(id=4, name=Louisa May Alcott)], genres=[Genre(id=5, name=novel)]), BookDto(id=5, title=Beloved, comments=[], authors=[Author(id=5, name=Toni Morrison)], genres=[Genre(id=5, name=novel)]), BookDto(id=6, title=Where the Crawdads Sing, comments=[], authors=[Author(id=6, name=Delia Owens)], genres=[Genre(id=5, name=novel)]), BookDto(id=7, title=The Glass Castle, comments=[], authors=[Author(id=7, name=Jeannette Walls)], genres=[Genre(id=6, name=unknown)]), BookDto(id=8, title=Carrie, comments=[Comment(id=3, book=Book(id=8, title=Carrie, authors=[Author(id=8, name=Stephen King)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)]), message=Scary book!), Comment(id=4, book=Book(id=8, title=Carrie, authors=[Author(id=8, name=Stephen King)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)]), message=JUST AMAZING)], authors=[Author(id=8, name=Stephen King)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)]), BookDto(id=9, title=Born a Crime, comments=[], authors=[Author(id=9, name=Trevor Noah)], genres=[Genre(id=6, name=unknown)]), BookDto(id=10, title=Bird Box, comments=[], authors=[Author(id=10, name=Josh Malerman)], genres=[Genre(id=5, name=novel)])]";
    private static final String COMMAND_FIND_NON_EXISTENT = "find --title 'New Book Title'";
    private static final String COMMAND_FIND = "find --title 'Carrie'";
    private static final String FIND_RESULT = "BookDto(id=8, title=Carrie, comments=[Comment(id=3, book=Book(id=8, title=Carrie, authors=[Author(id=8, name=Stephen King)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)]), message=Scary book!), Comment(id=4, book=Book(id=8, title=Carrie, authors=[Author(id=8, name=Stephen King)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)]), message=JUST AMAZING)], authors=[Author(id=8, name=Stephen King)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)])";
    private static final String COMMAND_SAVE = "i --title 'New Book Title' --genre 'classic' --author 'First Author' --comment 'Great Book!'";
    private static final String SUCCESSFULLY_SAVED = "Book successfully added to library";
    private static final String SAVED_BOOK = "BookDto(id=11, title=New Book Title, comments=[Comment(id=6, book=Book(id=11, title=New Book Title, authors=[Author(id=12, name=First Author)], genres=[Genre(id=8, name=classic)]), message=Great Book!)], authors=[Author(id=12, name=First Author)], genres=[Genre(id=8, name=classic)])";
    private static final String COMMAND_UPDATE = "u --id 8 --title 'Updated title' --genre 'Updated genre' --author 'Updated author' --comment 'Updated comment'";
    private static final String SUCCESSFULLY_UPDATED = "Book successfully updated";
    private static final String COMMAND_FIND_UPDATED = "find --title 'Updated title'";
    private static final String UPDATED_BOOK = "BookDto(id=8, title=Updated title, comments=[Comment(id=5, book=Book(id=8, title=Updated title, authors=[Author(id=11, name=Updated author)], genres=[Genre(id=7, name=Updated genre)]), message=Updated comment)], authors=[Author(id=11, name=Updated author)], genres=[Genre(id=7, name=Updated genre)])";
    private static final String COMMAND_ADD_AUTHOR = "author --id 8 --name 'New Author'";
    private static final String SUCCESSFULLY_ADD_AUTHOR = "Author was added successfully";
    private static final String BOOK_AFTER_ADD_AUTHOR = "BookDto(id=8, title=Carrie, comments=[Comment(id=3, book=Book(id=8, title=Carrie, authors=[Author(id=8, name=Stephen King), Author(id=13, name=New Author)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)]), message=Scary book!), Comment(id=4, book=Book(id=8, title=Carrie, authors=[Author(id=8, name=Stephen King), Author(id=13, name=New Author)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)]), message=JUST AMAZING)], authors=[Author(id=8, name=Stephen King), Author(id=13, name=New Author)], genres=[Genre(id=3, name=horror), Genre(id=5, name=novel)])";
    
    @DisplayName(" should prints all books in library")
    @Test
    void shouldPrintsAllBooks() {
        String res = (String) shell.evaluate(() -> COMMAND_LIST);
        assertThat(res).isEqualTo(String.format(BOOKS_LIST));
    }

    @DisplayName(" should find book by title")
    @Test
    void shouldFindBookByTitle(){
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res).isEqualTo(String.format(FIND_RESULT));
    }

    @DisplayName(" should throw NoSuchElementException if book not found")
    @Test
    void shouldThrowExceptionIfBookNotFound(){
        Throwable throwable = (Throwable) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(throwable).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName(" should correctly save book in library")
    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void shouldCorrectlySaveBook() {
        Throwable throwable = (Throwable) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(throwable).isInstanceOf(NoSuchElementException.class);

        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_SAVE));
        assertThat(res).isEqualTo(String.format(SUCCESSFULLY_SAVED));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND_NON_EXISTENT));
        assertThat(res).isEqualTo(String.format(SAVED_BOOK));
    }

    @DisplayName(" should correctly update book in library")
    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void shouldCorrectlyAddAuthor() {
        String res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res).isEqualTo(String.format(FIND_RESULT));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_ADD_AUTHOR));
        assertThat(res).isEqualTo(String.format(SUCCESSFULLY_ADD_AUTHOR));

        res = (String) shell.evaluate(new InputWithCommandArguments(COMMAND_FIND));
        assertThat(res).isEqualTo(String.format(BOOK_AFTER_ADD_AUTHOR));
    }
}
