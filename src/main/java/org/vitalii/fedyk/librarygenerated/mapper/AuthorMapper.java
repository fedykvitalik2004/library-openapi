package org.vitalii.fedyk.librarygenerated.mapper;

import org.mapstruct.Mapper;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarygenerated.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toAuthor(CreateAuthorDto createAuthorDto);
    ReadAuthorDto toReadAuthorDto(Author author);
}
