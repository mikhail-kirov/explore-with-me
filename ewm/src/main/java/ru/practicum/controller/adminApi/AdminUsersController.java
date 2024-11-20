package ru.practicum.controller.adminApi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.service.adminService.AdminUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUsersController {

    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUser) {
        log.info("Admin-запрос на добавление пользователя: {}, {}", newUser.getName(), newUser.getEmail());
        return adminUserService.createUser(newUser);
    }

    @GetMapping
    public List<UserDto> getAllUserDtoFromRange(@RequestParam(value = "ids", required = false) List<Long> ids,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Admin-запрос на получение списка пользователей: {}", ids);
        return adminUserService.getAllUserDtoFromRange(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        log.info("Admin-запрос на удаление пользователя с ID {}", userId);
        adminUserService.removeUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
