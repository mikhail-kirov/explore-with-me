package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.data.adminData.AdminUserRepository;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.model.User;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidUser {

    private final AdminUserRepository adminUserRepository;

    public User validUserById(Long userId) {
        if (userId == null) {
            throw new BadRequestException("User with id = null", "Bad request.");
        }
        return adminUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Incorrect input parameters","User not found"));
    }
}
