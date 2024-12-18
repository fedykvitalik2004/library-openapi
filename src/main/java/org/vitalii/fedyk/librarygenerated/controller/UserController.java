package org.vitalii.fedyk.librarygenerated.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.librarygenerated.api.controller.UsersApi;
import org.vitalii.fedyk.librarygenerated.api.dto.*;
import org.vitalii.fedyk.librarygenerated.service.BorrowedBookService;
import org.vitalii.fedyk.librarygenerated.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController implements UsersApi {
    private UserService userService;
    private BorrowedBookService borrowedBookService;

    @Override
    public ReadUserDto createUser(CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Override
    public List<ReadBorrowedBookDto> findBorrowedBooks(Long userId) {
        return borrowedBookService.getBorrowedBooksForUser(userId);
    }

    @Override
    public ReadUsersDto getAllUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Override
    public ReadUserDto getUserById(Long id) {
        return userService.readUser(id);
    }

    @Override
    public ReadUserDto updateUser(Long id, CreateUserDto createUserDto) {
        return userService.updateUser(id, createUserDto);
    }
}
