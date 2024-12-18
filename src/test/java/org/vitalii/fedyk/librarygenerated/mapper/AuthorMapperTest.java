package org.vitalii.fedyk.librarygenerated.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarygenerated.api.dto.FullNameDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadAuthorDto;
import org.vitalii.fedyk.librarygenerated.model.Author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AuthorMapperImpl.class)
class AuthorMapperTest {
    @Autowired
    private AuthorMapper authorMapper;

    @Test
    void toAuthor() {
        final CreateAuthorDto createAuthorDto = new CreateAuthorDto();
        createAuthorDto.setFullName(new FullNameDto("John", "Doe"));
        createAuthorDto.setDescription("Description");
        final Author author = authorMapper.toAuthor(createAuthorDto);
        assertNotNull(author);
        assertEquals("John", author.getFullName().getFirstName());
        assertEquals("Doe", author.getFullName().getLastName());
        assertEquals("Description", author.getDescription());
    }

    @Test
    void toReadAuthorDto() {
        final Author author = new Author(null, new FullNameDto("John", "Doe"), "Description", null);
        final ReadAuthorDto readAuthorDto = authorMapper.toReadAuthorDto(author);
        assertNotNull(readAuthorDto);
        assertEquals("John", readAuthorDto.getFullName().getFirstName());
        assertEquals("Doe", readAuthorDto.getFullName().getLastName());
        assertEquals("Description", readAuthorDto.getDescription());
    }
}
