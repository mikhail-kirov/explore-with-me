package ru.practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.adminData.AdminCategoriesRepository;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.mapper.MappingCategory;
import ru.practicum.model.Category;
import ru.practicum.validation.ValidCategory;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminCategoriesServiceImpl implements AdminCategoriesService {

    private final AdminCategoriesRepository adminCategoriesRepository;
    private final ValidCategory validCategory;

    @Override
    public CategoryDto postCategory(CategoryDto categoryDto) {
        Category category = MappingCategory.toCategory(categoryDto);
        return saveCategory(category);
    }

    @Override
    public CategoryDto patchCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = validCategory.validCategoryById(categoryId);
        validCategory.validExistNameCategory(category, categoryDto.getName());
        return saveCategory(MappingCategory.toCategory(categoryId, categoryDto));
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long id) {
        Category category = validCategory.validCategoryById(id);
        validCategory.validExistEventOfCategory(category);
        try {
            adminCategoriesRepository.deleteById(id);
            log.info("Категория {} удалена", category.getName());
        } catch (RuntimeException e) {
            log.info("Ошибка удаления категории {}", category.getName());
            throw new IncorrectParameterException(e.getMessage(), "For the requested operation the conditions are not met.");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private CategoryDto saveCategory(Category category) {
        try {
            category = adminCategoriesRepository.save(category);
            log.info("Категория {} сохранена", category.getName());
        } catch (RuntimeException e) {
            log.info("Ошибка сохранения категории");
            throw new IncorrectParameterException(e.getMessage(), "Incorrectly made request.");
        }
        return MappingCategory.toCategoryDto(category);
    }
}
