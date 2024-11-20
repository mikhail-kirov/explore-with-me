package ru.practicum.service.adminService;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    UserDto createUser(NewUserRequest newUser);

    List<UserDto> getAllUserDtoFromRange(List<Long> ids, Integer from, Integer size);

    ResponseEntity<Void> removeUser(Long id);
}
