package org.vitalii.fedyk.librarygenerated.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.FullNameDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUsersDto;
import org.vitalii.fedyk.librarygenerated.exception.EmailAlreadyUsedException;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.mapper.UserMapperImpl;
import org.vitalii.fedyk.librarygenerated.model.User;
import org.vitalii.fedyk.librarygenerated.repository.UserRepository;
import org.vitalii.fedyk.librarygenerated.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BorrowedBookService borrowedBookService;
    @Spy
    private UserMapperImpl userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    private final long userId = 1L;
    private User user;
    private CreateUserDto createUserDto;
    private Pageable pageable;

    @BeforeEach
    void clean() {
        user = getUser();
        createUserDto = getCreateUserDto();
        pageable = PageRequest.of(0, 2);
    }

    @Test
    void testReadUser_SuccessfulRetrieval() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final ReadUserDto result = userService.readUser(userId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("email@mail.com", result.getEmail());
        assertEquals(new FullNameDto("John", "Doe"), result.getFullName());
        assertEquals(LocalDate.of(2024, 5, 28), result.getBirthday());

        verify(userRepository).findById(userId);
        verify(userMapper).toReadUserDto(user);
    }

    @Test
    void testReadUser_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.readUser(userId));

        verify(userRepository).findById(userId);
        verify(userMapper, never()).toReadUserDto(any());
    }

    @Test
    void testDeleteUser_UserHasBorrowedBooks() {
        when(borrowedBookService.isBorrowedByUser(userId)).thenReturn(true);

        assertThrows(
                OperationNotPermittedException.class,
                () -> userService.deleteUser(userId)
        );

        verify(borrowedBookService).isBorrowedByUser(userId);
        verify(userRepository, never()).deleteById(userId);
        verify(userRepository, never()).findById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(borrowedBookService.isBorrowedByUser(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));

        verify(borrowedBookService).isBorrowedByUser(1L);
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testDeleteUser_SuccessfulDeletion() {
        when(borrowedBookService.isBorrowedByUser(userId))
                .thenReturn(false);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(borrowedBookService).isBorrowedByUser(userId);
        verify(userRepository).findById(userId);
        verify(userRepository).delete(any(User.class));
    }

    @Test
    void testFindAll_SuccessfulRetrieval() {
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));

        final ReadUsersDto result = userService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getUsers().size());

        verify(userRepository).findAll(pageable);
    }

    @Test
    void testFindAll_EmptyResult() {
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.EMPTY_LIST));

        final ReadUsersDto result = userService.findAll(pageable);

        assertEquals(0L, result.getUsers().size());

        verify(userRepository).findAll(pageable);
    }

    @Test
    void testCreateUser_EmailAlreadyUsed() {
        when(userRepository.exists(any())).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> userService.createUser(createUserDto));

        verify(userRepository).exists(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toUser(any());
        verify(userMapper, never()).toReadUserDto(any());
    }

    @Test
    void testCreateUser_SuccessfulCreation() {
        when(userRepository.exists(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        final ReadUserDto result = userService.createUser(createUserDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("email@mail.com", result.getEmail());
        assertEquals(new FullNameDto("John", "Doe"), result.getFullName());
        assertEquals(LocalDate.of(2024, 5, 28), result.getBirthday());

        verify(userRepository).exists(any());
        verify(userMapper).toUser(createUserDto);
        verify(userRepository).save(any());
        verify(userMapper).toReadUserDto(user);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, createUserDto));

        verify(userRepository).findById(userId);
        verify(userRepository, never()).existsByEmailAndIdNot(any(), any());
        verify(userMapper, never()).updateUserFromCreateUserDto(any(), any());
        verify(userMapper, never()).toReadUserDto(any());
    }

    @Test
    void testUpdateUser_EmailAlreadyUsed() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(createUserDto.getEmail(), userId)).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> userService.updateUser(userId, createUserDto));

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmailAndIdNot(createUserDto.getEmail(), userId);
        verify(userMapper, never()).updateUserFromCreateUserDto(any(), any());
        verify(userMapper, never()).toReadUserDto(any());
    }

    @Test
    void testUpdateUser_SuccessfulUpdate() {
        when(userRepository.existsByEmailAndIdNot(createUserDto.getEmail(), userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final ReadUserDto result = userService.updateUser(userId, createUserDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("email@mail.com", result.getEmail());
        assertEquals(new FullNameDto("John", "Doe"), result.getFullName());
        assertEquals(LocalDate.of(2024, 5, 28), result.getBirthday());

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmailAndIdNot(createUserDto.getEmail(), userId);
        verify(userMapper).updateUserFromCreateUserDto(createUserDto, user);
        verify(userMapper).toReadUserDto(any());
    }

}
