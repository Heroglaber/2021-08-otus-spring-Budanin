package ru.otus.library.models.domain;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "comments")
public class Comment {
    @Transient
    public static final String SEQUENCE_NAME = "comments_sequence";

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
//    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "book_id", nullable = false)
    @DBRef(lazy = true)
    private Book book;
    @NonNull
//    @Column(name = "message", nullable = false)
    private String message;
}
