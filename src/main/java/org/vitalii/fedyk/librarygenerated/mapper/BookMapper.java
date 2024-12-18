package org.vitalii.fedyk.librarygenerated.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBookDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadBookDto;
import org.vitalii.fedyk.librarygenerated.model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "author.id", source = "authorId")
    Book toBook(CreateBookDto createBookDto);
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorFullName", source = "author.fullName")
    ReadBookDto toReadBookDto(Book book);
}
