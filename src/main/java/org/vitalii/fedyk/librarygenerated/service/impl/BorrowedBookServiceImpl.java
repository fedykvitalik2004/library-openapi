package org.vitalii.fedyk.librarygenerated.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.mapper.BorrowedBookMapper;
import org.vitalii.fedyk.librarygenerated.model.Book;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBook;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBookId;
import org.vitalii.fedyk.librarygenerated.repository.BookRepository;
import org.vitalii.fedyk.librarygenerated.repository.BorrowedBookRepository;
import org.vitalii.fedyk.librarygenerated.repository.UserRepository;
import org.vitalii.fedyk.librarygenerated.service.BorrowedBookService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.vitalii.fedyk.librarygenerated.constant.ExceptionConstants.*;

@Service
@AllArgsConstructor
public class BorrowedBookServiceImpl implements BorrowedBookService {
    private final UserRepository userRepository;
    private BookRepository bookRepository;
    private BorrowedBookRepository borrowedBookRepository;
    private BorrowedBookMapper borrowedBookMapper;

    @Override
    public ReadBorrowedBookDto add(CreateBorrowedBookDto createBorrowedBookDto) {
        if (borrowedBookRepository.existsById(new BorrowedBookId(createBorrowedBookDto.getBookId(),
                createBorrowedBookDto.getUserId()))) {
            throw new OperationNotPermittedException(BORROWED_BOOK_ALREADY_EXISTS);
        }
        final BorrowedBook borrowedBook = borrowedBookMapper.toBorrowedBook(createBorrowedBookDto);
        final Book book = bookRepository.findById(createBorrowedBookDto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException(
                        BOOK_NOT_FOUND_BY_ID.formatted(createBorrowedBookDto.getBookId())));
        if (!userRepository.existsById(createBorrowedBookDto.getUserId())) {
            throw new IllegalArgumentException(USER_NOT_FOUND_BY_ID);
        }
        borrowedBook.setBorrowDate(ZonedDateTime.now());
        return borrowedBookMapper.toBorrowedBookDto(borrowedBookRepository.save(borrowedBook), book);
    }

    @Override
    public boolean isBorrowedByUser(final Long userId) {
        return borrowedBookRepository.existsByBorrowedBookIdUserId(userId);
    }

    @Override
    public boolean isBorrowedByBook(final Long bookId) {
        return borrowedBookRepository.existsByBorrowedBookIdBookId(bookId);
    }

    @Override
    public void remove(final Long bookId, final Long userId) {
        final BorrowedBook borrowedBook = borrowedBookRepository.findById(new BorrowedBookId(bookId, userId))
                .orElseThrow(() -> new NotFoundException(BORROWED_BOOK_NOT_FOUND));
        borrowedBookRepository.delete(borrowedBook);
    }

    @Override
    public List<ReadBorrowedBookDto> getBorrowedBooksForUser(final Long userId) {
        final List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserId(userId)
                .stream().toList();
        final List<Book> books = bookRepository.findAllByIds(borrowedBooks.stream()
                .map(book -> book.getBorrowedBookId().getBookId()).toList());
        final List<ReadBorrowedBookDto> readBorrowedBooks= new ArrayList<>();
        for ( int i = 0; i < borrowedBooks.size(); i++ ) {
            readBorrowedBooks.add(borrowedBookMapper.toBorrowedBookDto(borrowedBooks.get(i), books.get(i)));
        }
        return readBorrowedBooks;
    }
}