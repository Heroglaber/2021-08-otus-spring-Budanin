package ru.otus.library.domain;

import lombok.Data;

@Data
public class Book {
    private long id;
    private final String title;
    private final long genreId;

    public Book(long id, String title, long genreId) {
        this.id = id;
        this.title = title;
        this.genreId = genreId;
    }

    public Book(String title, long genreId) {
        this.title = title;
        this.genreId = genreId;
    }
}
