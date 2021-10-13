package ru.otus.library.models.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"title", "authors"})
public class BookDTO {
    private long id;
    @NonNull
    private String title;
    private List<AuthorDTO> authors;
    private List<GenreDTO> genres;

    public BookDTO(@NonNull String title) {
        this.title = title;
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public void addAuthor(AuthorDTO authorDTO){
        if(!this.authors.contains(authorDTO)) {
            this.authors.add(authorDTO);
        }
        else {
            throw new RuntimeException("The book already has such an author.");
        }
    }

    public void addGenre(GenreDTO genreDTO) {
        if(!this.genres.contains(genreDTO)) {
            this.genres.add(genreDTO);
        }
        else {
            throw new RuntimeException("The book already has such a genre.");
        }
    }
}
