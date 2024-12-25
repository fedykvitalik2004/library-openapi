package org.vitalii.fedyk.librarygenerated.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarygenerated.api.dto.*;
import org.vitalii.fedyk.librarygenerated.exception.EmailAlreadyUsedException;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.mapper.UserMapperImpl;
import org.vitalii.fedyk.librarygenerated.model.FullName;
import org.vitalii.fedyk.librarygenerated.model.User;
import org.vitalii.fedyk.librarygenerated.repository.UserRepository;
import org.vitalii.fedyk.librarygenerated.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void testReadUser_SuccessfulRetrieval() {
        final long userId = 1L;
        final User user = new User(
                userId,
                new FullName("John", "Doe"),
                "email@mail.com",
                "password",
                LocalDate.of(2000, 12, 21));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final ReadUserDto result = userService.readUser(userId);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFullName().getFirstName(), result.getFullName().getFirstName());
        assertEquals(user.getFullName().getLastName(), result.getFullName().getLastName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getBirthday(), result.getBirthday());

        verify(userRepository).findById(userId);
        verify(userMapper).toReadUserDto(user);
    }

    @Test
    void testReadUser_UserNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.readUser(1L));

        verify(userRepository).findById(any());
        verify(userMapper, never()).toReadUserDto(any());
    }

    @Test
    void testDeleteUser_UserHasBorrowedBooks() {
        final long userId = 1L;
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
        final long userId = 1L;
        when(borrowedBookService.isBorrowedByUser(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));

        verify(borrowedBookService).isBorrowedByUser(1L);
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testDeleteUser_SuccessfulDeletion() {
        final long userId = 1L;
        final User user = new User(
                userId,
                new FullName("John", "Doe"),
                "email@mail.com",
                "password",
                LocalDate.of(2000, 12, 21));

        when(borrowedBookService.isBorrowedByUser(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(borrowedBookService).isBorrowedByUser(userId);
        verify(userRepository).findById(userId);
        verify(userRepository).delete(any(User.class));
    }

    @Test
    void testFindAll_SuccessfulRetrieval() {
        final User user = new User(
                1L,
                new FullName("John", "Doe"),
                "email@mail.com",
                "password",
                LocalDate.of(2000, 12, 21));
        final Pageable pageable = Pageable.ofSize(2);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));

        final ReadUsersDto result = userService.findAll(pageable);
        final ReadUserDto element = result.getUsers().getFirst();

        assertNotNull(result);
        assertEquals(1, result.getUsers().size());
        assertEquals(user.getId(), element.getId());
        assertEquals(user.getEmail(), element.getEmail());
        assertEquals(user.getFullName().getFirstName(), element.getFullName().getFirstName());
        assertEquals(user.getFullName().getLastName(), element.getFullName().getLastName());
        assertEquals(user.getBirthday(), element.getBirthday());

        verify(userRepository).findAll(pageable);
    }

    @Test
    void testFindAll_EmptyResult() {
        final Pageable pageable = Pageable.ofSize(25);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        final ReadUsersDto result = userService.findAll(pageable);

        assertEquals(0, result.getUsers().size());

        verify(userRepository).findAll(pageable);
    }

    @Test
    void testCreateUser_EmailAlreadyUsed() {
        final CreateUserDto createUserDto = new CreateUserDto(
                new FullNameDto("John", "Doe"),
                "email@mail.com",
                "Password",
                LocalDate.of(2000, 12, 21));
        when(userRepository.exists(any())).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> userService.createUser(createUserDto));

        verify(userRepository).exists(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toUser(any());
        verify(userMapper, never()).toReadUserDto(any());
    }

    @Test
    void testCreateUser_SuccessfulCreation() {
        final CreateUserDto createUserDto = new CreateUserDto(
                new FullNameDto("John", "Doe"),
                "email@mail.com",
                "Password",
                LocalDate.of(2000, 12, 21));
        final User user = new User(
                1L,
                new FullName("John", "Doe"),
                "email@mail.com",
                "Password",
                LocalDate.of(2000, 12, 21));
        when(userRepository.exists(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        final ReadUserDto result = userService.createUser(createUserDto);

        assertEquals(createUserDto.getEmail(), result.getEmail());
        assertEquals(createUserDto.getFullName(), result.getFullName());
        assertEquals(createUserDto.getBirthday(), result.getBirthday());

        verify(userRepository).exists(any());
        verify(userMapper).toUser(createUserDto);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toReadUserDto(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        final long userId = 1L;
        final CreateUserDto createUserDto = new CreateUserDto(
                new FullNameDto("John", "Doe"),
                "email@mail.com",
                "Password",
                LocalDate.of(2000, 12, 21));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, createUserDto));

        verify(userRepository).findById(userId);
        verify(userRepository, never()).existsByEmailAndIdNot(any(), any());
        verify(userMapper, never()).updateUserFromCreateUserDto(any(), any());
        verify(userMapper, never()).toReadUserDto(any());
    }

    @Test
    void testUpdateUser_EmailAlreadyUsed() {
        final long userId = 1L;
        final User user = new User(
                userId,
                new FullName("John", "Doe"),
                "email@mail.com",
                "Password",
                LocalDate.of(2000, 12, 21));
        final CreateUserDto createUserDto = new CreateUserDto(
                new FullNameDto("John", "Doe"),
                "email@mail.com",
                "Password",
                LocalDate.of(2000, 12, 21));
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
        final ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        final long userId = 1L;
        final CreateUserDto createUserDto = new CreateUserDto(
                new FullNameDto("Jane", "Do"),
                "email@gmail.com",
                "password",
                LocalDate.of(1997, 3, 29));
        final User user = new User(
                userId,
                new FullName("John", "Doe"),
                "email@mail.com",
                "Password",
                LocalDate.of(2000, 12, 21));


        when(userRepository.existsByEmailAndIdNot(createUserDto.getEmail(), userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final ReadUserDto result = userService.updateUser(userId, createUserDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(createUserDto.getEmail(), result.getEmail());
        assertEquals(createUserDto.getFullName(), result.getFullName());
        assertEquals(createUserDto.getBirthday(), result.getBirthday());

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmailAndIdNot(createUserDto.getEmail(), userId);
        verify(userMapper).updateUserFromCreateUserDto(eq(createUserDto), argumentCaptor.capture());
        verify(userMapper).toReadUserDto(any());

        final User capturedUser = argumentCaptor.getValue();
        assertEquals(createUserDto.getPassword(), capturedUser.getPassword());
    }
}