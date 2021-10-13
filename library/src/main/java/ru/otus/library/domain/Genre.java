package ru.otus.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Genre {
    private long id;
    @NotBlank
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
