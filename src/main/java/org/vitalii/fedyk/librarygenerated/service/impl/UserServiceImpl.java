package org.vitalii.fedyk.librarygenerated.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.fedyk.librarygenerated.api.dto.*;
import org.vitalii.fedyk.librarygenerated.exception.EmailAlreadyUsedException;
import org.vitalii.fedyk.librarygenerated.exception.NotFoundException;
import org.vitalii.fedyk.librarygenerated.exception.OperationNotPermittedException;
import org.vitalii.fedyk.librarygenerated.mapper.UserMapper;
import org.vitalii.fedyk.librarygenerated.model.User;
import org.vitalii.fedyk.librarygenerated.repository.UserRepository;
import org.vitalii.fedyk.librarygenerated.service.BorrowedBookService;
import org.vitalii.fedyk.librarygenerated.service.UserService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import static org.vitalii.fedyk.librarygenerated.constant.ExceptionConstants.*;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    private UserRepository userRepository;
    private BorrowedBookService borrowedBookService;

    @Override
    public ReadUserDto createUser(final CreateUserDto createUserDto) {
        if (userRepository.exists(Example.of(new User().setEmail(createUserDto.getEmail())))) {
            throw new EmailAlreadyUsedException(EMAIL_ALREADY_USED.formatted(createUserDto.getEmail()));
        }
        final User user = userMapper.toUser(createUserDto);
        return userMapper.toReadUserDto(userRepository.save(user));
    }

    @Override
    public ReadUserDto readUser(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_ID.formatted(id)));
        return userMapper.toReadUserDto(user);
    }

    @Override
    public ReadUserDto updateUser(final Long id, final CreateUserDto createUserDto) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_ID.formatted(id)));
        if (userRepository.existsByEmailAndIdNot(createUserDto.getEmail(), id)) {
            throw new EmailAlreadyUsedException(EMAIL_ALREADY_USED.formatted(createUserDto.getEmail()));
        }
        userMapper.updateUserFromCreateUserDto(createUserDto, user);
        return userMapper.toReadUserDto(user);
    }

    @Override
    public void deleteUser(final Long id) {
        if (borrowedBookService.isBorrowedByUser(id)) {
            throw new OperationNotPermittedException(USER_CANNOT_BE_DELETED);
        }
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_ID.formatted(id)));
        userRepository.delete(user);
    }

    @Override
    public ReadUsersDto findAll(final Pageable pageable) {
        return createUsersDto(userRepository.findAll(pageable));
    }

    private ReadUsersDto createUsersDto(final Page<User> users) {
        ReadUsersDto readUsersDto = new ReadUsersDto();
        readUsersDto.setUsers(users.getContent().stream().map(userMapper::toReadUserDto).toList());
        final PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPageNumber(users.getNumber());
        paginationDto.setPageSize(users.getSize());
        paginationDto.setTotalNumberOfPages(users.getTotalPages());
        readUsersDto.setPagination(paginationDto);
        return readUsersDto;
    }
}