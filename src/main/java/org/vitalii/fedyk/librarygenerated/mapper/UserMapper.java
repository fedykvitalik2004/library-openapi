package org.vitalii.fedyk.librarygenerated.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUserDto;
import org.vitalii.fedyk.librarygenerated.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserDto createUserDto);
    ReadUserDto toReadUserDto(User user);
    void updateUserFromCreateUserDto(CreateUserDto createUserDto, @MappingTarget User user);
}
