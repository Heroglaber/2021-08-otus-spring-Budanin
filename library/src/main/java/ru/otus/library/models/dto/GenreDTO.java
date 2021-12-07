package ru.otus.library.models.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class GenreDTO {
    private String id;
    private String name;

    public GenreDTO(String name) {
        this.name = name;
    }
}
