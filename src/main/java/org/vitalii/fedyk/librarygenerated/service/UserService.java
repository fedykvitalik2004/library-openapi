package org.vitalii.fedyk.librarygenerated.service;

import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUsersDto;

public interface UserService {
    ReadUserDto createUser(CreateUserDto createUserDto);
    ReadUserDto readUser(Long id);
    ReadUserDto updateUser(Long id, CreateUserDto createUserDto);
    void deleteUser(Long id);
    ReadUsersDto findAll(Pageable pageable);
}
