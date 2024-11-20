package ru.practicum.controller.publicApi;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.publicService.PublicCategoriesService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoriesController {

    private final PublicCategoriesService publicCategoriesService;

    @GetMapping
    public List<CategoryDto> getAllCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Public-запрос на получение списка категорий в диапазоне от {} до {}", from, from + size);
        return publicCategoriesService.getAllCategories(from, size);
    }

    @GetMapping(("/{catId}"))
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Public-запрос на получение данных категории с ID: {}", catId);
        return publicCategoriesService.getCategoryById(catId);
    }
}
