package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("BookService must ")
@SpringBootTest
public class BookServiceIntegrationTest {
    private static final String AUTHOR_NAME = "Arkady Strugatsky";
    private static final String FANTASY_GENRE = "fantasy";
    private static final String CLASSIC_GENRE = "classic";
    private static final String BOOK_TITLE = "The Final Circle of Paradise";
    private static final String NEW_BOOK_TITLE = "The War and Peace";
    private static final String ADVENTURE_BOOK = "The Three Musketeers";
    private static final String ADVENTURE_GENRE = "adventure";
    private static final String ADVENTURE_AUTHOR = "Alexandre Dumas";

    @Autowired
    private BookService bookService;
}
