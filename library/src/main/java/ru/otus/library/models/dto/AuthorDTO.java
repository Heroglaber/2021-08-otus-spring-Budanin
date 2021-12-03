package ru.otus.library.models.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class AuthorDTO {
    private String id;
    private String name;
    private List<BookDTO> books;

    public AuthorDTO(String name) {
        this.name = name;
    }
}
