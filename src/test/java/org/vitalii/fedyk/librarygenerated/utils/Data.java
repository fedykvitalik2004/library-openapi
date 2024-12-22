package org.vitalii.fedyk.librarygenerated.utils;

import org.vitalii.fedyk.librarygenerated.api.dto.*;
import org.vitalii.fedyk.librarygenerated.model.Author;
import org.vitalii.fedyk.librarygenerated.model.Book;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBook;
import org.vitalii.fedyk.librarygenerated.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Data {
    public static Author getAuthor(Long authorId) {
        return new Author(authorId, new FullNameDto("John", "Doe"), "Description", null);
    }

    public static Book getBook(Long bookId, Long authorId) {
        return new Book(bookId, "Title", "Description", BookGenreDto.FANTASY, (short) 100, getAuthor(authorId));
    }

    public static BorrowedBook getBorrowedBook(Long bookId, Long userId) {
        return new BorrowedBook(bookId, userId, ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0), ZoneId.of("UTC")),
                ZonedDateTime.of(LocalDateTime.of(2024, 5, 28, 5, 49), ZoneId.of("UTC")));
    }

    public static User getUser() {
        return new User(1L, new FullNameDto("John", "Doe"), "email@mail.com", "password", LocalDate.of(2024, 5, 28));
    }

    public static ReadUserDto getReadUserDto() {
        return new ReadUserDto()
                .id(1L)
                .fullName(new FullNameDto("John", "Doe"))
                .email("email@mail.com")
                .birthday(LocalDate.of(2024, 5, 28));

    }

    public static CreateBookDto getCreateBookDto(Long authorId) {
        CreateBookDto createBookDto = new CreateBookDto();
        createBookDto.setTitle("Title");
        createBookDto.setDescription("Description");
        createBookDto.setGenre(BookGenreDto.FANTASY);
        createBookDto.setPagesCount(100);
        createBookDto.setAuthorId(authorId);
        return createBookDto;
    }

    public static CreateUserDto getCreateUserDto() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setFullName(new FullNameDto("John", "Doe"));
        createUserDto.setEmail("email@mail.com");
        createUserDto.setPassword("password");
        createUserDto.setBirthday(LocalDate.of(2024, 5, 28));
        return createUserDto;
    }

    public static CreateBorrowedBookDto getCreateBorrowedBookDto(final long bookId, final long userId) {
        CreateBorrowedBookDto createBorrowedBookDto = new CreateBorrowedBookDto();
        createBorrowedBookDto.setBookId(bookId);
        createBorrowedBookDto.setUserId(userId);
        createBorrowedBookDto.setReturnDate(ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0), ZoneId.of("UTC")));
        return createBorrowedBookDto;
    }
}
