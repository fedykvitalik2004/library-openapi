package org.vitalii.fedyk.librarygenerated.model;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class BorrowedBookId implements Serializable {
    private Long bookId;
    private Long userId;
}