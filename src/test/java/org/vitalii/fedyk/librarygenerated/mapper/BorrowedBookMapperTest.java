package org.vitalii.fedyk.librarygenerated.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.model.Book;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.vitalii.fedyk.librarygenerated.utils.Data.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BorrowedBookMapperImpl.class)
class BorrowedBookMapperTest {
    @Autowired
    BorrowedBookMapper borrowedBookMapper;

    @Test
    void toBorrowedBook() {
        final CreateBorrowedBookDto createBorrowedBookDto = getCreateBorrowedBookDto(1L, 1L);
        final BorrowedBook borrowedBook = borrowedBookMapper.toBorrowedBook(createBorrowedBookDto);
        assertNotNull(borrowedBook);
        assertEquals(createBorrowedBookDto.getBookId(), borrowedBook.getBookId());
        assertEquals(createBorrowedBookDto.getUserId(), borrowedBook.getUserId());
        assertEquals(createBorrowedBookDto.getReturnDate(), borrowedBook.getReturnDate());
    }

    @Test
    void toBorrowedBookDto() {
        BorrowedBook borrowedBook = getBorrowedBook(1L, 1L);
        Book book = getBook(1L, 1L);
        ReadBorrowedBookDto readBorrowedBookDto = borrowedBookMapper.toBorrowedBookDto(borrowedBook, book);
        assertNotNull(readBorrowedBookDto);
        assertEquals(borrowedBook.getUserId(), readBorrowedBookDto.getUserId());
        assertEquals(borrowedBook.getBorrowDate(), readBorrowedBookDto.getBorrowDate());
        assertEquals(borrowedBook.getReturnDate(), readBorrowedBookDto.getReturnDate());
        assertEquals(readBorrowedBookDto.getBook().getId(), borrowedBook.getBookId());
    }
}
