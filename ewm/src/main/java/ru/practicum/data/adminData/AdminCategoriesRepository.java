package ru.practicum.data.adminData;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Category;

public interface AdminCategoriesRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
