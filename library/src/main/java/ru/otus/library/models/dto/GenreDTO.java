package ru.otus.library.models.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class GenreDTO {
    private String id;
    @NonNull
    private String name;
}
