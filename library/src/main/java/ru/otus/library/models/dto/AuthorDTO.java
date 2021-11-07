package ru.otus.library.models.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class AuthorDTO {
    private String id;
    @NonNull
    private String name;
    private List<BookDTO> books;
}
