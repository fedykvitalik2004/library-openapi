package org.vitalii.fedyk.librarygenerated.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.vitalii.fedyk.librarygenerated.api.dto.BookGenreDto;

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
    private BookGenreDto genre;
    private short pagesCount;
    @ManyToOne
    private Author author;
}