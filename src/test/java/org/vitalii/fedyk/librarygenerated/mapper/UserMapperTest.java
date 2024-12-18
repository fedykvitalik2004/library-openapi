package org.vitalii.fedyk.librarygenerated.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUserDto;
import org.vitalii.fedyk.librarygenerated.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getCreateUserDto;
import static org.vitalii.fedyk.librarygenerated.utils.Data.getUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapperImpl.class})
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void toUser() {
        final CreateUserDto createUserDto = getCreateUserDto();
        final User user = userMapper.toUser(createUserDto);
        assertNotNull(user);
        assertEquals(createUserDto.getFullName().getFirstName(), user.getFullName().getFirstName());
        assertEquals(createUserDto.getFullName().getLastName(), user.getFullName().getLastName());
        assertEquals(createUserDto.getEmail(), user.getEmail());
        assertEquals(createUserDto.getPassword(), user.getPassword());
        assertEquals(createUserDto.getBirthday(), user.getBirthday());
    }

    @Test
    void toReadUserDto() {
        final User user = getUser(1L);
        final ReadUserDto readUserDto = userMapper.toReadUserDto(user);
        assertNotNull(readUserDto);
        assertEquals(user.getId(), readUserDto.getId());
        assertEquals(user.getFullName().getFirstName(), readUserDto.getFullName().getFirstName());
        assertEquals(user.getFullName().getLastName(), readUserDto.getFullName().getLastName());
        assertEquals(user.getEmail(), readUserDto.getEmail());
        assertEquals(user.getBirthday(), readUserDto.getBirthday());
    }

    @Test
    void updateUserFromCreateUserDto() {
        final User initialUser = getUser(1L);
        final User user = getUser(1L);
        final CreateUserDto createUserDto = getCreateUserDto();

        userMapper.updateUserFromCreateUserDto(createUserDto, user);

        assertNotNull(user);
        assertEquals(createUserDto.getFullName().getFirstName(), user.getFullName().getFirstName());
        assertEquals(createUserDto.getFullName().getLastName(), user.getFullName().getLastName());
        assertEquals(createUserDto.getEmail(), user.getEmail());
        assertEquals(createUserDto.getPassword(), user.getPassword());
        assertEquals(createUserDto.getBirthday(), user.getBirthday());
    }
}
