package ru.practicum.mapper;

import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.model.User;

import java.util.List;

public class MappingUser {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getEmail().trim(), user.getId(), user.getName().trim());
    }

    public static User toUser(NewUserRequest newUser) {
        return new User(null, newUser.getName(), newUser.getEmail());
    }

    public static List<UserDto> toUserDto(List<User> users) {
        return users.stream().map(MappingUser::toUserDto).toList();
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
