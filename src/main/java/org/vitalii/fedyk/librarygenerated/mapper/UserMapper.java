package org.vitalii.fedyk.librarygenerated.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.vitalii.fedyk.librarygenerated.api.dto.CreateUserDto;
import org.vitalii.fedyk.librarygenerated.api.dto.FullNameDto;
import org.vitalii.fedyk.librarygenerated.api.dto.ReadUserDto;
import org.vitalii.fedyk.librarygenerated.model.FullName;
import org.vitalii.fedyk.librarygenerated.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", source = "fullName", qualifiedByName = "toFullName")
    User toUser(CreateUserDto createUserDto);
    @Mapping(target = "fullName", source = "fullName", qualifiedByName = "toFullNameDto")
    ReadUserDto toReadUserDto(User user);
    void updateUserFromCreateUserDto(CreateUserDto createUserDto, @MappingTarget User user);

    @Named("toFullName")
    default FullName toFullName(final FullNameDto fullNameDto) {
        return new FullName(fullNameDto.getFirstName(), fullNameDto.getLastName());
    }

    @Named("toFullNameDto")
    default FullNameDto toFullName(final FullName fullName) {
        return new FullNameDto(fullName.getFirstName(), fullName.getLastName());
    }
}
