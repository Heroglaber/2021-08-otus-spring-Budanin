package ru.otus.library.domain;

import lombok.Data;

@Data
public class BookAuthor {
    private final long bookId;
    private final long authorId;
}
