package ru.otus.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookDto {
    private long id;
    @EqualsAndHashCode.Include
    private String title;
    @EqualsAndHashCode.Include
    private List<Comment> comments;
    @EqualsAndHashCode.Include
    private List<Author> authors;
    @EqualsAndHashCode.Include
    private List<Genre> genres;

    public BookDto() {
        this.comments = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public void setAuthors(List<Author> authors) {
        this.authors = new ArrayList<>(authors);
    }

    public void setGenres(List<Genre> genres) {
        this.genres = new ArrayList<>(genres);
    }

    public void setComments(List<Comment> comments) {
        this.comments = new ArrayList<>(comments);
    }

    public Book extractBook() {
        return new Book(this.id, this.title, this.authors, this.genres);
    }
}
