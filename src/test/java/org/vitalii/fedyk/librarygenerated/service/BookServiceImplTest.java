package org.vitalii.fedyk.librarygenerated.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBookDto;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.model.Book;
import org.vitalii.fedyk.librarygenerated.repository.BookRepository;
import org.vitalii.fedyk.librarygenerated.search.BookSpecification;
import org.vitalii.fedyk.librarygenerated.search.SpecificationsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getCreateBookDto;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getCreateBookDtoForUpdate;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookServiceImplTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CreateBookDto createBookDto;
    private final long userId = 10L;
    private final long authorId = 10L;
    private final long borrowedBookId = 10L;
    private final long notBorrowedBookId = 11L;
    private final long nonExistentBookId = 200L;
    private final long nonExistentAuthorId = 400L;
    private final String pathForRemovingData = "src/main/resources/removal.sql";

    @BeforeEach
    void clear() {
        createBookDto = getCreateBookDto();
    }

    @Test
    @Sql("/data.sql")
    void testReadBook_Success() {
        final ReadBookDto result = bookService.readBook(userId);
        assertNotNull(result);
    }

    @Test
    @Sql("/data.sql")
    void testReadBook_NotFound() {
        assertThrows(NotFoundException.class, () -> bookService.readBook(nonExistentBookId));
    }

    @Test
    @Sql("/data.sql")
    void testAuthorHasBooks() {
        final boolean result = bookService.authorHasBooks(authorId);
        assertTrue(result);
    }

    @Test
    @Sql("/data.sql")
    void testDeleteBook_BookBorrowed() {
        assertEquals(2, bookRepository.findAll().size());
        assertThrows(OperationNotPermittedException.class, () -> bookService.deleteBook(borrowedBookId));
        assertEquals(2, bookRepository.findAll().size());
    }

    @Test
    @Sql("/data.sql")
    void testDeleteBook_BookNotFound() {
        assertEquals(2, bookRepository.findAll().size());
        assertThrows(NotFoundException.class, () -> bookService.deleteBook(nonExistentBookId));
        assertEquals(2, bookRepository.findAll().size());
    }

    @Test
    @Sql("/data.sql")
    void testDeleteBook_Success() {
        assertEquals(2, bookRepository.findAll().size());
        bookService.deleteBook(notBorrowedBookId);
        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    @Sql("/data.sql")
    void testCreateBook_Success() {
        assertEquals(2, bookRepository.findAll().size());
        bookService.createBook(createBookDto);
        assertEquals(3, bookRepository.findAll().size());
    }

    @Test
    @Sql("/data.sql")
    void testCreateBook_AuthorNotExists() {
        createBookDto.setAuthorId(nonExistentAuthorId);
        assertThrows(NotFoundException.class, () -> bookService.createBook(createBookDto));
    }

    @Test
    @Sql("/data.sql")
    void testSearch_Success () {
        final SpecificationsBuilder<Book> builder = new SpecificationsBuilder<>();
        builder.with("title", ":", "Title");
        final List<ReadBookDto> result = bookService.search(builder.build(BookSpecification::new));
        assertEquals(1, result.size());
    }

    @Test
    @Sql("/data.sql")
    void testSearch_NonExistentField () {
        final SpecificationsBuilder<Book> builder = new SpecificationsBuilder<>();
        builder.with("non_existent", ":", "Title");
        final List<ReadBookDto> result = bookService.search(builder.build(BookSpecification::new));
        assertEquals(2, result.size(), "If the field doesn't exist, then the result will contain all data");
    }

    @Test
    @Sql("/data.sql")
    void updateBook_Success() {
        final CreateBookDto createBookDtoForUpdate = getCreateBookDtoForUpdate();

        final ReadBookDto result = bookService.updateBook(borrowedBookId, createBookDtoForUpdate);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("Updated", result.getTitle());
        assertEquals("Updated description", result.getDescription());
        assertEquals(1000, result.getPagesCount());
        assertEquals(11, result.getAuthorId());
    }

    @Test
    @Sql("/data.sql")
    void updateBook_AuthorNotFound() {
        final CreateBookDto createBookDtoForUpdate = getCreateBookDtoForUpdate();
        createBookDtoForUpdate.setAuthorId(nonExistentAuthorId);

        assertThrows(NotFoundException.class, () -> bookService.updateBook(borrowedBookId, createBookDtoForUpdate));
    }

    @Test
    @Sql("/data.sql")
    void updateBook_BookNotFound() {
        final CreateBookDto createBookDtoForUpdate = getCreateBookDtoForUpdate();

        assertThrows(NotFoundException.class, () -> bookService.updateBook(nonExistentBookId, createBookDtoForUpdate));
    }

    @AfterEach
    void deleteDataInDatabase() throws IOException {
        final String sql = new String(Files.readAllBytes(Path.of(pathForRemovingData)));
        jdbcTemplate.execute(sql);
    }
}