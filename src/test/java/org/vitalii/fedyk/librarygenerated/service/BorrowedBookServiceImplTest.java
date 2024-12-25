package org.vitalii.fedyk.librarygenerated.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitalii.fedyk.librarygenerated.api.dto.BookGenreDto;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.FullNameDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.enumeraton.BookGenre;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.mapper.BorrowedBookMapperImpl;
import org.vitalii.fedyk.librarygenerated.model.*;
import org.vitalii.fedyk.librarygenerated.repository.BookRepository;
import org.vitalii.fedyk.librarygenerated.repository.BorrowedBookRepository;
import org.vitalii.fedyk.librarygenerated.repository.UserRepository;
import org.vitalii.fedyk.librarygenerated.service.impl.BorrowedBookServiceImpl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void testIsBorrowedByUser_BookBorrowed() {
        final long userId = 1L;
        when(borrowedBookRepository.existsByBorrowedBookIdUserId(userId)).thenReturn(true);

        final boolean result = borrowedBookService.isBorrowedByUser(userId);

        assertTrue(result);

        verify(borrowedBookRepository).existsByBorrowedBookIdUserId(userId);
    }

    @Test
    void testIsBorrowedByUser_BookNotBorrowed() {
        final long userId = 1L;
        when(borrowedBookRepository.existsByBorrowedBookIdUserId(userId)).thenReturn(false);

        final boolean result = borrowedBookService.isBorrowedByUser(userId);

        assertFalse(result);

        verify(borrowedBookRepository).existsByBorrowedBookIdUserId(userId);
    }

    @Test
    void testIsBorrowedByBook_BookBorrowed() {
        final long bookId = 1L;
        when(borrowedBookRepository.existsByBorrowedBookIdBookId(bookId)).thenReturn(true);

        final boolean result = borrowedBookService.isBorrowedByBook(bookId);

        assertTrue(result);

        verify(borrowedBookRepository).existsByBorrowedBookIdBookId(bookId);
    }

    @Test
    void testIsBorrowedByBook_BookNotBorrowed() {
        final long bookId = 1L;
        when(borrowedBookRepository.existsByBorrowedBookIdBookId(bookId)).thenReturn(false);

        final boolean result = borrowedBookService.isBorrowedByBook(bookId);

        assertFalse(result);

        verify(borrowedBookRepository).existsByBorrowedBookIdBookId(bookId);
    }

    @Test
    void testRemove_Success() {
        final BorrowedBookId borrowedBookId = new BorrowedBookId(1L, 2L);
        final BorrowedBook borrowedBook = new BorrowedBook(
                borrowedBookId,
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(120));
        when(borrowedBookRepository.findById(borrowedBookId))
                .thenReturn(Optional.of(borrowedBook));

        borrowedBookService.remove(borrowedBookId.getBookId(), borrowedBookId.getUserId());

        verify(borrowedBookRepository).findById(any(BorrowedBookId.class));
        verify(borrowedBookRepository).delete(any(BorrowedBook.class));
    }

    @Test
    void testRemove_NotFound() {
        when(borrowedBookRepository.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> borrowedBookService.remove(1L, 1L));

        verify(borrowedBookRepository).findById(any(BorrowedBookId.class));
        verify(borrowedBookRepository, never()).delete(any(BorrowedBook.class));
    }

    @Test
    void testAdd_Success() {
        final ArgumentCaptor<BorrowedBook> argumentCaptor = ArgumentCaptor.forClass(BorrowedBook.class);
        final long bookId = 1L;
        final long userId = 2L;
        final Author author = new Author(3L, new FullName("Jane", "Doe"), "Description", new ArrayList<>());
        final Book book = new Book(bookId, "Title", "Description", BookGenre.BIOGRAPHY, 120, author);
        author.getBooks().add(book);
        final CreateBorrowedBookDto createBorrowedBookDto = new CreateBorrowedBookDto(
                bookId,
                userId,
                ZonedDateTime.now().plusDays(30)
        );
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.existsById(userId)).thenReturn(true);
        when(borrowedBookRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        final ReadBorrowedBookDto result = borrowedBookService.add(createBorrowedBookDto);

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository).findById(bookId);
        verify(userRepository).existsById(userId);
        verify(borrowedBookRepository).save(argumentCaptor.capture());

        assertAll(
                () -> assertEquals(createBorrowedBookDto.getBookId(), result.getBook().getId()),
                () -> assertEquals(createBorrowedBookDto.getUserId(), result.getUserId()),
                () -> assertEquals(createBorrowedBookDto.getReturnDate(), result.getReturnDate())
        );

        final BorrowedBook borrowedBook = argumentCaptor.getValue();
        assertNotNull(borrowedBook.getBorrowDate());
    }

    @Test
    void testAdd_AlreadyBorrowed() {
        final long bookId = 1L;
        final long userId = 2L;
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(true);

        assertThrows(OperationNotPermittedException.class, () -> borrowedBookService.add(new CreateBorrowedBookDto()));

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository, never()).findById(bookId);
        verify(userRepository, never()).existsById(userId);
        verify(borrowedBookRepository, never()).save(any());
    }

    @Test
    void testAdd_BookNotFound() {
        final long bookId = 1L;
        final long userId = 2L;
        final CreateBorrowedBookDto createBorrowedBookDto = new CreateBorrowedBookDto(
                bookId,
                userId,
                ZonedDateTime.now().plusDays(30)
        );
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> borrowedBookService.add(createBorrowedBookDto));

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository).findById(bookId);
        verify(userRepository, never()).existsById(userId);
        verify(borrowedBookRepository, never()).save(any(BorrowedBook.class));
    }

    @Test
    void testAdd_UserNotFound() {
        final long bookId = 1L;
        final long userId = 2L;
        final CreateBorrowedBookDto createBorrowedBookDto = new CreateBorrowedBookDto(
                bookId,
                userId,
                ZonedDateTime.now().plusDays(30)
        );
        when(borrowedBookRepository.existsById(any(BorrowedBookId.class))).thenReturn(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> borrowedBookService.add(createBorrowedBookDto));

        verify(borrowedBookRepository).existsById(any(BorrowedBookId.class));
        verify(bookRepository).findById(bookId);
        verify(userRepository).existsById(userId);
        verify(borrowedBookRepository, never()).save(any(BorrowedBook.class));
    }

    @Test
    void testGetBorrowedBooksForUser_Success() {
        final long bookId = 1L;
        final long userId = 2L;
        final BorrowedBookId borrowedBookId = new BorrowedBookId(bookId, userId);
        final BorrowedBook borrowedBook = new BorrowedBook(
                borrowedBookId,
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(120));
        final Author author = new Author(3L, new FullName("Jane", "Doe"), "Description", new ArrayList<>());
        final Book book = new Book(bookId, "Title", "Description", BookGenre.BIOGRAPHY, 120, author);
        author.getBooks().add(book);
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