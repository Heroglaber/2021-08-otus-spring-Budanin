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

public class BookRepositoryCustomImpl implements BookRepositoryCustom{
    private final MongoTemplate mongoTemplate;

    public BookRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addBookRefToAuthors(Book book) {
        Book bookRef = new Book(book.getId(), book.getTitle());
        List<String> authorsId = book.getAuthors().stream()
                .map(Author::getId).collect(Collectors.toList());
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(authorsId));
        Update update = new Update();
        update.addToSet("books", bookRef);
        mongoTemplate.updateMulti(query, update, Author.class);
    }

    @Override
    public void deleteBookRefFromAuthors(Book book) {
        val query = Query.query(Criteria.where("_id").is(new ObjectId(book.getId())));
        val update = new Update().pull("books", query);
        mongoTemplate.updateMulti(new Query(), update, Author.class);
    }


}
