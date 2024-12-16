package org.vitalii.fedyk.librarygenerated.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadAuthorDto;

public interface AuthorService {
    ReadAuthorDto createAuthor(CreateAuthorDto createAuthorDto);

    ReadAuthorDto readAuthor(Long id);

    ReadAuthorDto updateAuthor(Long id, CreateAuthorDto readAuthorDto);

    void deleteAuthor(Long id);

    Page<ReadAuthorDto> findAll(Pageable pageable);
}
