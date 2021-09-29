package ru.otus.library.models.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class GenreDTO {
    private long id;
    @NonNull
    private String name;
}
