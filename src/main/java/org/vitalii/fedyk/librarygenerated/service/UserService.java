package org.vitalii.fedyk.librarygenerated.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUserDto;

public interface UserService {
    ReadUserDto createUser(CreateUserDto createUserDto);
    ReadUserDto readUser(Long id);
    ReadUserDto updateUser(Long id, CreateUserDto createUserDto);
    void deleteUser(Long id);
    Page<ReadUserDto> findAll(Pageable pageable);
}
