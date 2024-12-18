package org.vitalii.fedyk.librarygenerated.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBookDto;
import org.vitalii.fedyk.librarygenerated.model.Book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getBook;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getCreateBookDto;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookMapperImpl.class, AuthorMapperImpl.class})
class BookMapperTest {
    @Autowired
    BookMapper bookMapper;

    @Test
    void toBook() {
        final CreateBookDto createBookDto = getCreateBookDto(1L);
        final Book book = bookMapper.toBook(createBookDto);
        assertNotNull(book);
        assertEquals(createBookDto.getTitle(), book.getTitle());
        assertEquals(createBookDto.getDescription(), book.getDescription());
        assertEquals(createBookDto.getGenre(), book.getGenre());
        assertEquals(createBookDto.getPagesCount(), book.getPagesCount());
        assertEquals(createBookDto.getAuthorId(), book.getAuthor().getId());
    }

    @Test
    void toReadBookDto() {
        final Book book = getBook(1L, 1L);
        final ReadBookDto readBookDto = bookMapper.toReadBookDto(book);
        assertNotNull(readBookDto);
        assertEquals(book.getId(), readBookDto.getId());
        assertEquals(book.getTitle(), readBookDto.getTitle());
        assertEquals(book.getDescription(), readBookDto.getDescription());
        assertEquals(book.getGenre(), readBookDto.getGenre());
        assertEquals(book.getPagesCount(), readBookDto.getPagesCount());
        assertEquals(book.getAuthor().getId(), readBookDto.getAuthorId());
        assertEquals(book.getAuthor().getFullName().getFirstName(), readBookDto.getAuthorFullName().getFirstName());
        assertEquals(book.getAuthor().getFullName().getLastName(), readBookDto.getAuthorFullName().getLastName());
    }
}