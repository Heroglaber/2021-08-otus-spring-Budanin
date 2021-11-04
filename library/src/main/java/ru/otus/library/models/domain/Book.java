package ru.otus.library.models.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "books")
public class Book {

    @Transient
    public static final String SEQUENCE_NAME = "books_sequence";

    @Id
    private long id;

    @NonNull
    //@Column(name = "title", nullable = false)
    private String title;

    //@Fetch(FetchMode.SUBSELECT)
    //@ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    //@JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"),
    //inverseJoinColumns = @JoinColumn(name = "author_id"))
    @DBRef
    private List<Author> authors;

//    @Fetch(FetchMode.SUBSELECT)
//    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
//    @JoinTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"),
//            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @DBRef
    private List<Genre> genres;
}
