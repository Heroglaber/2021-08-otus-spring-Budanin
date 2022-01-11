package ru.otus.library.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.services.BookService;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @DisplayName("correctly return all books")
    @Test
    public void shouldReturnAllBooks() throws Exception {
        BookDTO book = toBookDTO("The Roadside Picnic",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy"));
        List<BookDTO> books = List.of(book);
        when(bookService.getAll()).thenReturn(books);

        mvc.perform(get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
