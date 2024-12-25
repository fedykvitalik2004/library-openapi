package org.vitalii.fedyk.librarygenerated.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.vitalii.fedyk.librarygenerated.enumeraton.BookGenre;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private BookGenre genre;
    private Integer pagesCount;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}