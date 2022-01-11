package ru.otus.library.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        List<BookDTO> books = List.of(toBookDTO("The Roadside Picnic",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy")));
        when(bookService.getAll()).thenReturn(books);

        mvc.perform(get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("The Roadside Picnic")));
    }

    @DisplayName("return book by id")
    @Test
    public void shouldReturnBookById() throws Exception {
        String id = "61b0a643595bdd0bf6720038";
        BookDTO book = toBookDTO("The Roadside Picnic",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy"));
        book.setId(id);

        when(bookService.getById(id)).thenReturn(book);

        mvc.perform(get("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)));

    }

    @DisplayName("add book")
    @Test
    public void shouldAddNewBook() throws Exception {
        String id = "61b0a643595bdd0bf6720038";
        BookDTO book = toBookDTO("The Roadside Picnic",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy"));
        book.setId(id);
        String json = "{\"id\":\"61b0a643595bdd0bf6720038\",\"title\":\"The Roadside Picnic\",\"authors\":[{\"id\":\"61ddc277fad43824d3718e8c\",\"name\":\"Arkady Strugatsky\",\"books\":null},{\"id\":\"61ddc277fad43824d3718e8d\",\"name\":\"Boris Strugatsky\",\"books\":null}],\"genres\":[{\"id\":\"61ddc277fad43824d3718e88\",\"name\":\"fantasy\"}]}";

        when(bookService.add(book)).thenReturn(book);

        mvc.perform(post("/book")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.authors[0].name", is("Arkady Strugatsky")));

    }

    @DisplayName("update book")
    @Test
    public void shouldUpdateBook() throws Exception {
        String id = "61b0a643595bdd0bf6720038";
        BookDTO book = toBookDTO("The Roadside Picnic",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy"));
        book.setId(id);
        String json = "{\"id\":\"61b0a643595bdd0bf6720038\",\"title\":\"The Roadside Picnic\",\"authors\":[{\"id\":\"61ddc277fad43824d3718e8c\",\"name\":\"Arkady Strugatsky\",\"books\":null},{\"id\":\"61ddc277fad43824d3718e8d\",\"name\":\"Boris Strugatsky\",\"books\":null}],\"genres\":[{\"id\":\"61ddc277fad43824d3718e88\",\"name\":\"fantasy\"}]}";

        when(bookService.update(book)).thenReturn(book);

        mvc.perform(post("/book/{id}", id)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.authors[0].name", is("Arkady Strugatsky")));

    }

    @DisplayName("delete book")
    @Test
    public void shouldDeleteBook() throws Exception {
        String id = "61b0a643595bdd0bf6720038";
        BookDTO book = toBookDTO("The Roadside Picnic",
                Arrays.asList("Arkady Strugatsky", "Boris Strugatsky"),
                Arrays.asList("fantasy"));
        book.setId(id);

        when(bookService.deleteById(id)).thenReturn(book);

        mvc.perform(delete("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.authors[0].name", is("Arkady Strugatsky")));

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
