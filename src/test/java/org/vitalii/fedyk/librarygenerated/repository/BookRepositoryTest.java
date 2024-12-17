package org.vitalii.fedyk.librarygenerated.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.vitalii.fedyk.librarygenerated.model.Author;
import org.vitalii.fedyk.librarygenerated.model.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.vitalii.fedyk.librarygenerated.utils.Entities.getBook;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    void findAllByIds_shouldReturnBooksForValidIds() {
        Book book = getBook();
        final Author author = authorRepository.save(book.getAuthor());
        book.setAuthor(author);
        book = bookRepository.save(book);

        List<Long> list = List.of(book.getId());
        List<Book> books = bookRepository.findAllByIds(list);

        assertNotNull(books, "Books should not be null");
        assertEquals(1, books.size(), "Books size should be 1");
        assertSame(book, books.get(0), "Books should be equal");
    }

    @Test
    void existsByAuthorId_shouldReturnTrueWhenAuthorHasBooks() {
        Book book = getBook();
        final Author author = authorRepository.save(book.getAuthor());
        book.setAuthor(author);
        bookRepository.save(book);

        assertTrue(bookRepository.existsByAuthorId(author.getId()));
    }

    @Test
    void existsByAuthorId_shouldReturnFalseWhenNoBooksExistForAuthor() {
        assertFalse(bookRepository.existsByAuthorId(-1L));
    }
}