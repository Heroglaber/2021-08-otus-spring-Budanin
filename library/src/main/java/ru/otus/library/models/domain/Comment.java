package ru.otus.library.models.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "comments")
public class Comment {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

//    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "book_id", nullable = false)
    @NonNull
    private Book book;

//    @Column(name = "message", nullable = false)
    @NonNull
    private String message;
}
