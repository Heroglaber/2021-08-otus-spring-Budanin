package ru.otus.library.repositories;

import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom{
    private final MongoTemplate mongoTemplate;

    public AuthorRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Book> getBooks(Author author) {
        List<String> booksId = author.getBooks().stream()
                .map(Book::getId).collect(Collectors.toList());
        Query query = new Query();
        query.addCriteria(Criteria.where("$id").in(booksId));
        return mongoTemplate.find(query, Book.class);
    }

    @Override
    public void deleteAuthorRefFromBooks(Author author) {
        val query = Query.query(Criteria.where("_id").is(new ObjectId(author.getId())));
        val update = new Update().pull("authors", query);
        mongoTemplate.updateMulti(new Query(), update, Book.class);
    }


}
