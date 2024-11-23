package ru.practicum.mapper;

import ru.practicum.dto.*;
import ru.practicum.model.Category;

import java.util.List;

public class MappingCategory {


    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(null, categoryDto.getName().trim());
    }

    public static Category toCategory(Long catId, CategoryDto categoryDto) {
        return new Category(catId, categoryDto.getName().trim());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static List<CategoryDto> toCategoryDto(List<Category> category) {
        return category.stream().map(MappingCategory::toCategoryDto).toList();
    }
}
