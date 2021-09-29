package ru.otus.library.models.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @NonNull
    @Column(name = "message", nullable = false)
    private String message;
}
