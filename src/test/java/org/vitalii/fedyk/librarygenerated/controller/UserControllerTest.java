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
import org.vitalii.fedyk.librarygenerated.api.dto.CreateUserDto;
import org.vitalii.fedyk.librarygenerated.repository.BorrowedBookRepository;
import org.vitalii.fedyk.librarygenerated.repository.UserRepository;
import org.vitalii.fedyk.librarygenerated.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getCreateUserDto;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;
    private final String partOfUrl = "/users";
    private final String pathForRemovingData = "src/main/resources/removal.sql";
    private final long existentUserId = 10L;
    private final long nonExistentUserId = 200L;
    private CreateUserDto createUserDto;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clear() {
        createUserDto = getCreateUserDto();
    }

    @Test
    @Sql("/data.sql")
    void testGetUsers_Success() throws Exception {
        mockMvc.perform(get(partOfUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.users[0].id").value(existentUserId));
    }

    @Test
    @Sql("/data.sql")
    void testGetUserById_Success() throws Exception {
        mockMvc.perform(get(partOfUrl + "/{userId}", existentUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existentUserId));
    }

    @Test
    @Sql("/data.sql")
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get(partOfUrl + "/{userId}", nonExistentUserId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete(partOfUrl + "/{userId}", nonExistentUserId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        mockMvc.perform(delete(partOfUrl + "/{userId}", existentUserId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testDeleteUser_Conflict() throws Exception {
        mockMvc.perform(delete(partOfUrl + "/{userId}", existentUserId))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testFindBorrowedBooks_Success() throws Exception {
        mockMvc.perform(get(partOfUrl + "/{userId}" + "/borrowed-books", existentUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].book.id").value(10));
    }

    @Test
    void testFindBorrowedBooks_EmptySuccessfulResult() throws Exception {
        mockMvc.perform(get(partOfUrl + "/{userId}" + "/borrowed-books", nonExistentUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        final long elementsBefore = userRepository.findAll().size();
        mockMvc.perform(post(partOfUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        final long elementsAfter = userRepository.findAll().size();
        assertEquals(1, elementsAfter - elementsBefore);
    }

    @Test
    void testCreateUser_EmptyNecessaryField() throws Exception {
        createUserDto.setEmail(null);
        mockMvc.perform(post(partOfUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testCreateUser_EmailUsed() throws Exception {
        createUserDto.setEmail("jane_doe@email.com");
        mockMvc.perform(post(partOfUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateUser_NotFound() throws Exception {
        createUserDto.setEmail("email@gmail.comm");
        mockMvc.perform(put(partOfUrl + "/{userId}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateUser_BadRequest() throws Exception {
        createUserDto.setEmail(null);
        mockMvc.perform(put(partOfUrl + "/{userId}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Sql("/data.sql")
    void testUpdateUser_Success() throws Exception {
        mockMvc.perform(put(partOfUrl + "/{userId}", existentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName.firstName").value(createUserDto.getFullName().getFirstName()))
                .andExpect(jsonPath("$.fullName.lastName").value(createUserDto.getFullName().getLastName()))
                .andExpect(jsonPath("$.email").value(createUserDto.getEmail()))
                .andExpect(jsonPath("$.birthday").value(createUserDto.getBirthday().toString()));
    }

    @AfterEach
    void deleteDataInDatabase() throws IOException {
        final String sql = new String(Files.readAllBytes(Path.of(pathForRemovingData)));
        jdbcTemplate.execute(sql);
    }
}
