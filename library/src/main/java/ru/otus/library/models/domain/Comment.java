package ru.otus.library.models.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @NonNull
    private Book book;

    @NonNull
    private String message;

    private LocalDateTime dateTime;
}
