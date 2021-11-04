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
@Document(collection = "authors")
public class Author {

    @Transient
    public static final String SEQUENCE_NAME = "authors_sequence";

    @Id
    private long id;

    @NonNull
    //@Column(name = "name", nullable = false, unique = true)
    private String name;

    @DBRef(lazy = true)
    private List<Book> books;
}
