package ru.practicum.service.adminService;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.CategoryDto;


public interface AdminCategoriesService {

    CategoryDto postCategory(CategoryDto category);

    CategoryDto patchCategory(Long categoryId, CategoryDto category);

    ResponseEntity<Void> deleteCategory(Long id);
}
