package ru.otus.library.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.library.models.domain.Book;
import ru.otus.library.repositories.BookRepository;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "abudanin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertTheRoadsidePicnic", author = "abudanin")
    public void insertBook(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("books");
        var doc = new Document().append("name", "The Roadside Picnic");
        myCollection.insertOne(doc);
    }

    @ChangeSet(order = "003", id = "insertCarrie", author = "abudanin")
    public void insertCarrie(BookRepository repository) {
        repository.save(new Book("Carrie"));
    }
}
