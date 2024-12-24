package org.vitalii.fedyk.librarygenerated.controller;

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
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.model.BorrowedBookId;
import org.vitalii.fedyk.librarygenerated.repository.BorrowedBookRepository;
import org.vitalii.fedyk.librarygenerated.service.BorrowedBookService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getCreateBorrowedBookDto;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class BorrowedBookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BorrowedBookService borrowedBookService;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private final String partOfUrl = "/borrowed-books";
    private final String pathForRemovingData = "src/main/resources/removal.sql";
    private CreateBorrowedBookDto createBorrowedBookDto;
    private final long bookId = 10L;
    private final long userId = 10L;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clear() {
        createBorrowedBookDto = getCreateBorrowedBookDto();
    }

    @Test
    @Sql("/data.sql")
    void testDelete_Success() throws Exception {
        final int borrowedBooksBefore = borrowedBookRepository.findAll().size();
        mockMvc.perform(delete(partOfUrl + "/{bookId}/{userId}", bookId, userId))
                .andExpect(status().isNoContent());
        final int borrowedBooksAfter = borrowedBookRepository.findAll().size();
        assertEquals(1, borrowedBooksBefore - borrowedBooksAfter);
    }

    @Test
    void testDelete_NonExistentBook() throws Exception {
        mockMvc.perform(delete(partOfUrl + "/{bookId}/{userId}", 200L, userId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDelete_NonExistentUser() throws Exception {
        mockMvc.perform(delete(partOfUrl + "/{bookId}/{userId}", bookId, 200L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testAddBorrowedBook_Success() throws Exception {
        assertFalse(borrowedBookRepository.existsById(new BorrowedBookId(11L, userId)));
        createBorrowedBookDto.setBookId(11L);
        createBorrowedBookDto.setUserId(userId);
        mockMvc.perform(post(partOfUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBorrowedBookDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertTrue(borrowedBookRepository.existsById(new BorrowedBookId(bookId, userId)));
    }

    @AfterEach
    void deleteDataInDatabase() throws IOException {
        final String sql = new String(Files.readAllBytes(Path.of(pathForRemovingData)));
        jdbcTemplate.execute(sql);
    }
}
