package ru.practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.adminData.AdminUserRepository;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.mapper.MappingUser;
import ru.practicum.model.User;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;

    @Override
    public UserDto createUser(NewUserRequest newUser) {
        if (newUser.getName() == null || newUser.getName().isEmpty()) {
            log.info("Некорректные параметры запроса");
            throw new BadRequestException("Field: name. Error: must not be blank. Value: null", "Incorrectly made request.");
        }
        User user;
        try {
            user = adminUserRepository.save(MappingUser.toUser(newUser));
        } catch (RuntimeException e) {
            log.info("Ошибка сохранения пользователя: {}", e.getMessage());
            throw new IncorrectParameterException(e.getMessage(), "Integrity constraint has been violated.");
        }
        return MappingUser.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUserDtoFromRange(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        try {
            if (ids == null || ids.isEmpty()) {
                users = adminUserRepository.getAllByPage(from, size);
            } else {
                users = adminUserRepository.getAllByIdIn(ids, from, size);
            }
        } catch (RuntimeException e) {
            log.info("Ошибка получения списка пользователей: {}", e.getMessage());
            throw new BadRequestException(e.getMessage(), "Incorrectly made request.");
        }
        return MappingUser.toUserDto(users);
    }

    @Override
    public ResponseEntity<Void> removeUser(Long userId) {
        adminUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found", "The required object was not found."));
        adminUserRepository.deleteById(userId);
        log.info("Пользователь с ID {} удален", userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
