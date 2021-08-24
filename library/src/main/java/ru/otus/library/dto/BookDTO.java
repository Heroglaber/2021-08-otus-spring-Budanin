package ru.otus.library.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private long id;
    private final String title;
    private final String genre;
    private List<String> authors;

    public BookDTO(String title, String genre, List<String> authors) {
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }

    public BookDTO(long id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }
    public BookDTO(long id, String title, String genre, List<String> authors) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }
}
