package ru.otus.library.models.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
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

    @Id
    private String id;

    @NonNull
    //@Column(name = "name", nullable = false, unique = true)
    private String name;

    private List<Book> books;
}
