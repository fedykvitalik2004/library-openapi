package org.vitalii.fedyk.librarygenerated.service;

import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadAuthorsDto;

public interface AuthorService {
    ReadAuthorDto createAuthor(CreateAuthorDto createAuthorDto);

    ReadAuthorDto readAuthor(Long id);

    ReadAuthorDto updateAuthor(Long id, CreateAuthorDto readAuthorDto);

    void deleteAuthor(Long id);

    ReadAuthorsDto findAll(Pageable pageable);
}
