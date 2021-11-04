package ru.otus.library.models.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "genres")
public class Genre {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

//    @Column(name = "name", nullable = false, unique = true)
    @NonNull
    private String name;
}
