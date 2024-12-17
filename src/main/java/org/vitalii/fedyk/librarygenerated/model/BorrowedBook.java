package org.vitalii.fedyk.librarygenerated.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Entity
@Table(name = "borrowed_books")
@IdClass(BorrowedBookId.class)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class BorrowedBook {
    @Id
    private Long bookId;
    @Id
    private Long userId;
    private ZonedDateTime borrowDate;
    private ZonedDateTime returnDate;
}