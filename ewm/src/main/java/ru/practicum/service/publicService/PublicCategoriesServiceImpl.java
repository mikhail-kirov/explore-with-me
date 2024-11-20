package ru.practicum.service.publicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.publicData.PublicCategoriesRepository;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.mapper.MappingCategory;
import ru.practicum.model.Category;
import ru.practicum.validation.ValidCategory;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCategoriesServiceImpl implements PublicCategoriesService {

    private final PublicCategoriesRepository publicCategories;
    private final ValidCategory validCategory;

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size){
        List<Category> categories = publicCategories.getAllCategories(from, size);
        if (!categories.isEmpty()){
            log.info("Список категорий отправлен");
            return MappingCategory.toCategoryDto(categories);
        }
        log.info("Список категорий по заданным параметрам пуст");
        return List.of();
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        if (categoryId == null) {
            log.info("Поле id не должно быть пустым");
            throw new BadRequestException("Field: id. Error: must not be blank. Value: null", "Incorrectly made request.");
        }
        Category category = validCategory.validCategoryById(categoryId);
        log.info("Категория '{}' отправлена", category.getName());
        return MappingCategory.toCategoryDto(category);
    }
}
