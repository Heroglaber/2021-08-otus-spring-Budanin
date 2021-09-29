package ru.otus.library.models.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"book", "message"})
public class CommentDTO {
    private long id;
    @NonNull
    private BookDTO book;
    @NonNull
    private String message;


    public String toString() {
        return "CommentDTO(id=" + this.getId() + ", bookTitle=" + this.getBook().getTitle() + ", message=" + this.getMessage() + ")\n";
    }
}
