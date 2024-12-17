package org.vitalii.fedyk.librarygenerated.utils;

import org.vitalii.fedyk.librarygenerated.api.dto.BookGenre;
import org.vitalii.fedyk.librarygenerated.api.dto.FullName;
import org.vitalii.fedyk.librarygenerated.model.Author;
import org.vitalii.fedyk.librarygenerated.model.Book;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBook;
import org.vitalii.fedyk.librarygenerated.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Entities {
    public static Author getAuthor() {
        return new Author(null, new FullName("John", "Doe"), "Description", null);
    }

    public static Book getBook() {
        return new Book(null, "Title", "Description", BookGenre.FANTASY, (short) 100, getAuthor());
    }

    public static BorrowedBook getBorrowedBook() {
        return new BorrowedBook(null, null, ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0), ZoneId.of("UTC")),
                ZonedDateTime.of(LocalDateTime.of(2024, 5, 28, 5, 49), ZoneId.of("UTC")));
    }

    public static User getUser() {
        return new User(null, new FullName("John", "Doe"), "email@mail.com", "password", LocalDate.of(2024, 5, 28));
    }
}
