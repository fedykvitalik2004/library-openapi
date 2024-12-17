package org.vitalii.fedyk.librarygenerated.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.vitalii.fedyk.librarygenerated.model.Author;
import org.vitalii.fedyk.librarygenerated.model.Book;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBook;
import org.vitalii.fedyk.librarygenerated.model.User;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.vitalii.fedyk.librarygenerated.utils.Entities.*;

@DataJpaTest
class BorrowedBookRepositoryTest {
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    void existsByUserId() {
        final User user = userRepository.save(getUser());
        userRepository.save(user);
        Book book = getBook();
        final Author author = authorRepository.save(book.getAuthor());
        book.setAuthor(author);
        book = bookRepository.save(book);
        BorrowedBook borrowedBook = getBorrowedBook()
                .setBookId(book.getId())
                .setUserId(user.getId());
        borrowedBook = borrowedBookRepository.save(borrowedBook);

        assertTrue(borrowedBookRepository.existsByUserId(user.getId()));
    }

    @Test
    void existsByBookId() {
        final User user = userRepository.save(getUser());
        userRepository.save(user);
        Book book = getBook();
        final Author author = authorRepository.save(book.getAuthor());
        book.setAuthor(author);
        book = bookRepository.save(book);
        BorrowedBook borrowedBook = getBorrowedBook()
                .setBookId(book.getId())
                .setUserId(user.getId());
        borrowedBook = borrowedBookRepository.save(borrowedBook);

        assertTrue(borrowedBookRepository.existsByBookId(book.getId()));
    }

    @Test
    void findByUserId() {
        final User user = userRepository.save(getUser());
        userRepository.save(user);
        Book book = getBook();
        final Author author = authorRepository.save(book.getAuthor());
        book.setAuthor(author);
        book = bookRepository.save(book);
        BorrowedBook borrowedBook = getBorrowedBook()
                .setBookId(book.getId())
                .setUserId(user.getId());
        borrowedBook = borrowedBookRepository.save(borrowedBook);

        assertSame(borrowedBookRepository.findByUserId(user.getId()).get(0), borrowedBook);
    }
}