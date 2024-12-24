package org.vitalii.fedyk.librarygenerated.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateAuthorDto;
import org.vitalii.fedyk.librarygenerated.repository.AuthorRepository;
import org.vitalii.fedyk.librarygenerated.service.AuthorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getCreateAuthorDto;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String pathOfUrl = "/authors";
    private final String pathForRemovingData = "src/main/resources/removal.sql";
    private final long existingReferencedAuthorId = 10L;
    private final long existingNotReferencedAuthorId = 11L;
    private CreateAuthorDto createAuthorDto;

    @BeforeEach
    void clear() {
        createAuthorDto = getCreateAuthorDto();
    }

    @Test
    @Sql("/data.sql")
    void testDeleteAuthor_OperationNotPermitted() throws Exception {
        mockMvc.perform(delete(pathOfUrl + "/{authorId}", existingReferencedAuthorId))
                .andExpect(status().isConflict());
        assertTrue(authorRepository.findById(existingReferencedAuthorId).isPresent());
    }

    @Test
    @Sql("/data.sql")
    void testDeleteAuthor_Success() throws Exception {
        mockMvc.perform(delete(pathOfUrl + "/{authorId}", existingNotReferencedAuthorId))
                .andExpect(status().isNoContent());
        assertTrue(authorRepository.findById(existingNotReferencedAuthorId).isEmpty());
    }

    @Test
    @Sql("/data.sql")
    void testDeleteAuthor_NotFound() throws Exception {
        mockMvc.perform(delete(pathOfUrl + "/{authorId}", 100L))
                .andExpect(status().isNotFound());
        assertFalse(authorRepository.findById(existingNotReferencedAuthorId).isEmpty());
    }

    @Test
    void testGetAuthorById_NotFound() throws Exception {
        mockMvc.perform(get(pathOfUrl + "/{authorId}", 100L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testGetAuthorById_Success() throws Exception {
        mockMvc.perform(get(pathOfUrl + "/{authorId}", existingReferencedAuthorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingReferencedAuthorId));
    }

    @Test
    @Sql("/data.sql")
    void testGetAllAuthors_Success() throws Exception {
        mockMvc.perform(get(pathOfUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authors[0].id").value(existingReferencedAuthorId));
    }

    @Test
    @Sql("/data.sql")
    void testCreateAuthor_Success() throws Exception {
        final long elementsBefore = authorRepository.findAll().size();
        mockMvc.perform(post(pathOfUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        final long elementsAfter = authorRepository.findAll().size();
        assertTrue(elementsAfter - elementsBefore == 1);
    }

    @Test
    @Sql("/data.sql")
    void testCreateAuthor_BadRequest() throws Exception {
        createAuthorDto.setFullName(null);
        mockMvc.perform(post(pathOfUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateAuthor_NotFound() throws Exception {
        mockMvc.perform(put(pathOfUrl + "/{authorId}", 300L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateAuthor_BadRequest() throws Exception {
        createAuthorDto.setFullName(null);
        mockMvc.perform(put(pathOfUrl + "/{authorId}", 300L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateAuthor_Success() throws Exception {
        mockMvc.perform(put(pathOfUrl + "/{authorId}", existingReferencedAuthorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @AfterEach
    void deleteDataInDatabase() throws IOException {
        final String sql = new String(Files.readAllBytes(Path.of(pathForRemovingData)));
        jdbcTemplate.execute(sql);
    }
}