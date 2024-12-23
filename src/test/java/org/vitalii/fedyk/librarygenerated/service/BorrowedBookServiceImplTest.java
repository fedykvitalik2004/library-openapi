package org.vitalii.fedyk.librarygenerated.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.mapper.BorrowedBookMapperImpl;
import org.vitalii.fedyk.librarygenerated.model.Book;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBook;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBookId;
import org.vitalii.fedyk.librarygenerated.repository.BookRepository;
import org.vitalii.fedyk.librarygenerated.repository.BorrowedBookRepository;
import org.vitalii.fedyk.librarygenerated.repository.UserRepository;
import org.vitalii.fedyk.librarygenerated.service.impl.BorrowedBookServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.*;

@ExtendWith(MockitoExtension.class)
class BorrowedBookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowedBookRepository borrowedBookRepository;
    @Mock
    private UserRepository userRepository;
    @Spy
    private BorrowedBookMapperImpl borrowedBookMapper;
    @InjectMocks
    private BorrowedBookServiceImpl borrowedBookService;
    private final long userId = 1L;
    private final long bookId = 2L;
    private Book book;
    private CreateBorrowedBookDto createBorrowedBookDto;
    private BorrowedBook borrowedBook;

    @BeforeEach
    void clear() {
        book = getBook();
        createBorrowedBookDto = getCreateBorrowedBookDto();
        borrowedBook = getBorrowedBook();
    }


    @Test
    void testIsBorrowedByUser_BookBorrowed() {
        when(borrowedBookRepository.existsByUserId(userId)).thenReturn(true);

        final boolean result = borrowedBookService.isBorrowedByUser(userId);

        assertTrue(result);

        verify(borrowedBookRepository).existsByUserId(userId);
    }

    @Test
    void testIsBorrowedByUser_BookNotBorrowed() {
        when(borrowedBookRepository.existsByUserId(userId)).thenReturn(false);

        final boolean result = borrowedBookService.isBorrowedByUser(userId);

        assertFalse(result);

        verify(borrowedBookRepository).existsByUserId(userId);
    }

    @Test
    void testIsBorrowedByBook_BookBorrowed() {
        when(borrowedBookRepository.existsByBookId(bookId)).thenReturn(true);

        final boolean result = borrowedBookService.isBorrowedByBook(bookId);

        assertTrue(result);

        verify(borrowedBookRepository).existsByBookId(bookId);
    }

    @Test
    void testIsBorrowedByBook_BookNotBorrowed() {
        when(borrowedBookRepository.existsByBookId(bookId)).thenReturn(false);

        final boolean result = borrowedBookService.isBorrowedByBook(bookId);

        assertFalse(result);

        verify(borrowedBookRepository).existsByBookId(bookId);
    }

    @Test
    void testRemove_Success() {
        when(borrowedBookRepository.findById(new BorrowedBookId(bookId, userId)))
                .thenReturn(Optional.of(getBorrowedBook()));

        borrowedBookService.remove(bookId, userId);

        verify(borrowedBookRepository).findById(any(BorrowedBookId.class));
        verify(borrowedBookRepository).delete(any(BorrowedBook.class));
    }

    @Test
    void testRemove_NotFound() {
        when(borrowedBookRepository.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> borrowedBookService.remove(bookId, userId));

        verify(borrowedBookRepository).findById(any(BorrowedBookId.class));
        verify(borrowedBookRepository, times(0)).delete(any(BorrowedBook.class));
    }

    @Test
    void testAdd_Success() {
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(false);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        borrowedBookService.add(createBorrowedBookDto);

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository).findById(bookId);
        verify(userRepository).existsById(userId);
        verify(borrowedBookRepository).save(any(BorrowedBook.class));
    }

    @Test
    void testAdd_AlreadyBorrowed() {
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(true);

        assertThrows(OperationNotPermittedException.class, () -> borrowedBookService.add(createBorrowedBookDto));

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository, never()).findById(bookId);
        verify(userRepository, never()).existsById(userId);
        verify(borrowedBookRepository, times(0)).save(any());
    }

    @Test
    void testAdd_BookNotFound() {
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(false);
        when(bookRepository.findById(bookId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> borrowedBookService.add(createBorrowedBookDto));

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository).findById(bookId);
        verify(userRepository, never()).existsById(userId);
        verify(borrowedBookRepository, never()).save(any(BorrowedBook.class));
    }

    @Test
    void testAdd_UserNotFound() {
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> borrowedBookService.add(createBorrowedBookDto));

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository).findById(bookId);
        verify(userRepository).existsById(userId);
        verify(borrowedBookRepository, never()).save(any(BorrowedBook.class));
    }

    @Test
    void testGetBorrowedBooksForUser_Success() {
        when(borrowedBookRepository.findByUserId(userId)).thenReturn(List.of(borrowedBook));
        when(bookRepository.findAllByIds(any())).thenReturn(List.of(book));

        final List<ReadBorrowedBookDto> result = borrowedBookService.getBorrowedBooksForUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());

        final ReadBorrowedBookDto readBorrowedBookDto = result.get(0);

        assertNotNull(readBorrowedBookDto);
        assertEquals(bookId, readBorrowedBookDto.getBook().getId());
        assertEquals(userId, readBorrowedBookDto.getUserId());
        assertEquals(borrowedBook.getBorrowDate(), readBorrowedBookDto.getBorrowDate());
        assertEquals(borrowedBook.getReturnDate(), readBorrowedBookDto.getReturnDate());

        verify(borrowedBookRepository).findByUserId(userId);
        verify(bookRepository).findAllByIds(any());
    }
}