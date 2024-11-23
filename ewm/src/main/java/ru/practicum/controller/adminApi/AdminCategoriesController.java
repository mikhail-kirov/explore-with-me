package ru.practicum.controller.adminApi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.adminService.AdminCategoriesService;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoriesController {

    private final AdminCategoriesService adminCategoriesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Admin-запрос на добавление категории: {}", categoryDto.getName());
        return adminCategoriesService.postCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable Long catId,
                                     @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Admin-запрос на изменение категории с ID: {}", catId);
        return adminCategoriesService.patchCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long catId) {
        log.info("Admin-запрос на удаление категории с ID {}", catId);
        return adminCategoriesService.deleteCategory(catId);
    }
}
