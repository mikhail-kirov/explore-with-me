package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.data.adminData.AdminCategoriesRepository;
import ru.practicum.data.privateData.PrivateEventRepository;
import ru.practicum.exeption.IncorrectParameterException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.model.Category;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidCategory {

    private final AdminCategoriesRepository adminCategoriesRepository;
    private final PrivateEventRepository privateEventRepository;

    public Category validCategoryById(Long categoryId) {
        return adminCategoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Incorrect input parameters","Category not found"));
    }

    public void validExistNameCategory(Category category, String categoryName) {
        if (adminCategoriesRepository.existsByName(categoryName) && !category.getName().equals(categoryName)) {
            log.info("Категория с именем {} уже существует", categoryName);
            throw new IncorrectParameterException("Категория с именем " + categoryName + " уже существует", "Incorrectly made request.");
        }
    }

    public void validExistEventOfCategory(Category category) {
        if (privateEventRepository.existsByCategory(category)) {
            log.info("Ошибка удаления категории. Существуют события с привязкой к категории {}", category.getName());
            throw new IncorrectParameterException("Не удалено. Существуют события с привязкой к категории " + category.getName(),
                    "Incorrectly made request.");
        }
    }
}
