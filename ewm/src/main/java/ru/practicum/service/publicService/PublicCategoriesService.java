package ru.practicum.service.publicService;

import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAllCategories(Integer from, Integer size);
    CategoryDto getCategoryById(Long categoryId);
}
