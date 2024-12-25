package org.vitalii.fedyk.librarygenerated.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateBorrowedBookDto;
import org.vitalii.fedyk.librarygenerated.repository.BorrowedBookRepository;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class BorrowedBookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql("/data.sql")
    void testDelete_Success() throws Exception {
        final long bookId = 1L;
        final long userId = 1L;
        final int borrowedBooksBefore = borrowedBookRepository.findAll().size();
        mockMvc.perform(delete("/borrowed-books/{bookId}/{userId}", bookId, userId))
                .andExpect(status().isNoContent());
        final int borrowedBooksAfter = borrowedBookRepository.findAll().size();
        assertEquals(1, borrowedBooksBefore - borrowedBooksAfter);
    }

    @Test
    void testDelete_NonExistentBook() throws Exception {
        final long booksBefore = borrowedBookRepository.count();
        mockMvc.perform(delete("/borrowed-books/{bookId}/{userId}", 1000L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        final long booksAfter = borrowedBookRepository.count();
        assertEquals(booksBefore, booksAfter);
    }

    @Test
    void testDelete_NonExistentUser() throws Exception {
        final long booksBefore = borrowedBookRepository.count();
        mockMvc.perform(delete("/borrowed-books/{bookId}/{userId}", 1L, 1000L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        final long booksAfter = borrowedBookRepository.count();
        assertEquals(booksBefore, booksAfter);
    }

    @Test
    @Sql("/data.sql")
    void testAddBorrowedBook_Success() throws Exception {
        final long borrowedBooksBefore = borrowedBookRepository.count();
        final CreateBorrowedBookDto createBorrowedBookDto = new CreateBorrowedBookDto();
        createBorrowedBookDto.setBookId(2L);
        createBorrowedBookDto.setUserId(1L);
        createBorrowedBookDto.setReturnDate(ZonedDateTime.now().plusDays(100));

        mockMvc.perform(post("/borrowed-books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBorrowedBookDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final long borrowedBooksAfter = borrowedBookRepository.count();
        assertEquals(1, borrowedBooksAfter - borrowedBooksBefore);
    }
}