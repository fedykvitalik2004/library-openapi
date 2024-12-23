package org.vitalii.fedyk.librarygenerated.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarygenerated.api.dto.FullNameDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadAuthorsDto;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.mapper.AuthorMapper;
import org.vitalii.fedyk.librarygenerated.model.Author;
import org.vitalii.fedyk.librarygenerated.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class AuthorServiceImplTest {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorMapper authorMapper;
    @MockitoBean
    private AuthorRepository authorRepository;
    @MockitoBean
    private BookService bookService;
    private final long authorId = 1L;
    private Author author;
    private CreateAuthorDto createAuthorDto;

    @BeforeEach
    void clear() {
        author = getAuthor();
        createAuthorDto = getCreateAuthorDto();
    }

    @Test
    void testReadAuthor_AuthorNotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.readAuthor(authorId));

        verify(authorRepository).findById(authorId);
    }

    @Test
    void testReadAuthor_SuccessfulRetrieval() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        final ReadAuthorDto result = authorService.readAuthor(authorId);

        assertNotNull(result);
        assertEquals(authorId, result.getId());
        assertEquals("John", author.getFullName().getFirstName());
        assertEquals("Doe", author.getFullName().getLastName());

        verify(authorRepository).findById(authorId);
    }

    @Test
    void testDeleteAuthor_AuthorNotFound() {
        when(authorRepository.existsById(authorId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> authorService.deleteAuthor(authorId));

        verify(authorRepository).existsById(authorId);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAuthor_AuthorHasBooks() {
        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(bookService.authorHasBooks(authorId)).thenReturn(true);

        assertThrows(OperationNotPermittedException.class, () -> authorService.deleteAuthor(authorId));

        verify(authorRepository).existsById(authorId);
        verify(bookService).authorHasBooks(authorId);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAuthor_SuccessfulDeletion() {
        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(bookService.authorHasBooks(authorId)).thenReturn(false);

        authorService.deleteAuthor(authorId);

        verify(authorRepository).deleteById(authorId);
        verify(authorRepository).existsById(authorId);
        verify(bookService).authorHasBooks(authorId);
    }

    @Test
    void testFindAll_AuthorsFound() {
        final Page<Author> authorsPage = new PageImpl<>(List.of(author));
        final Pageable pageable = PageRequest.of(0, 1);
        when(authorRepository.findAll(pageable)).thenReturn(authorsPage);

        final ReadAuthorsDto result = authorService.findAll(pageable);

        final ReadAuthorDto element = result.getAuthors().get(0);
        assertNotNull(result);
        assertEquals(1, result.getAuthors().size());
        assertEquals("John", element.getFullName().getFirstName());
        assertEquals("Doe", element.getFullName().getLastName());
        assertEquals("Description", element.getDescription());

        verify(authorRepository).findAll(pageable);
    }

    @Test
    void testFindAll_NoAuthorsFound() {
        final Page<Author> authorsPage = new PageImpl<>(List.of());
        final Pageable pageable = PageRequest.of(0, 1);
        when(authorRepository.findAll(pageable)).thenReturn(authorsPage);

        final ReadAuthorsDto result = authorService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.getAuthors().isEmpty());

        verify(authorRepository).findAll(pageable);
    }

    @Test
    void testCreateAuthor_SuccessfulCreation() {
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        final ReadAuthorDto result = authorService.createAuthor(createAuthorDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFullName().getFirstName());
        assertEquals("Doe", result.getFullName().getLastName());
        assertEquals("Description", result.getDescription());

        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor_AuthorNotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.updateAuthor(authorId, createAuthorDto));

        verify(authorRepository).findById(authorId);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor_SuccessfulUpdate() {
        createAuthorDto.setFullName(new FullNameDto("Jane", "Do"));
        createAuthorDto.setDescription("Changed");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        final ReadAuthorDto result = authorService.updateAuthor(authorId, createAuthorDto);

        assertNotNull(result);
        assertEquals("Jane", author.getFullName().getFirstName());
        assertEquals("Do", author.getFullName().getLastName());
        assertEquals("Changed", author.getDescription());

        verify(authorRepository).findById(authorId);
        verify(authorRepository).save(any(Author.class));
    }
}
