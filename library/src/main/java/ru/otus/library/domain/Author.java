package ru.otus.library.domain;

import lombok.Data;

@Data
public class Author {
    private long id;
    private final String name;

    public Author(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
