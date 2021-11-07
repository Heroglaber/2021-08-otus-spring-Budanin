package ru.otus.library.repositories;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Book;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookRepositoryCustomImpl implements BookRepositoryCustom{
    private final MongoTemplate mongoTemplate;

    public BookRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addBookRefToAuthors(String bookId) {
        Book book = mongoTemplate.findById(bookId, Book.class);
        Map<String, String> bookRef = Map.of(
                "id", book.getId(),
                "title", book.getTitle()
        );
        List<String> authorsId = book.getAuthors().stream()
                .map(Author::getId).collect(Collectors.toList());
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(authorsId));
        Update update = new Update();
        update.addToSet("books", bookRef);
        mongoTemplate.updateMulti(query, update, Author.class);
    }
}
