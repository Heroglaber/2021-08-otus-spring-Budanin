package ru.otus.library.models.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"book", "message"})
public class CommentDTO {
    private String id;
    @NonNull
    private BookDTO book;
    @NonNull
    private String message;

    private LocalDateTime dateTime = LocalDateTime.now();
}
