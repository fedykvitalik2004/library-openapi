package org.vitalii.fedyk.librarygenerated.service;

import org.springframework.data.jpa.domain.Specification;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBookDto;
import org.vitalii.fedyk.librarygenerated.model.Book;

import java.util.List;

public interface BookService {
    ReadBookDto createBook(CreateBookDto createBookDto);
    ReadBookDto readBook(Long id);
    ReadBookDto updateBook(Long id, CreateBookDto createBookDto);
    void deleteBook(Long id);
    List<ReadBookDto> search(Specification<Book> specification);
    boolean authorHasBooks(Long authorId);
}
