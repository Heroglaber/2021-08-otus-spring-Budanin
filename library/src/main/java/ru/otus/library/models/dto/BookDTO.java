package ru.otus.library.models.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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


    public String toString() {
        return "BookDTO(id=" + this.getId() +
                ", title=" + this.getTitle() +
                ", authors=" + this.getAuthors() +
                ", genres=" + this.getGenres() + ")\n";
    }
}
