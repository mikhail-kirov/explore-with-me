package ru.practicum.data.publicData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Category;

import java.util.List;

public interface PublicCategoriesRepository extends JpaRepository<Category, Long> {

    @Query("select cat " +
            "from Category as cat " +
            "order by cat.id asc " +
            "limit ?2 offset ?1")
    List<Category> getAllCategories(Integer from, Integer size);

    boolean existsByIdIn(List<Long> ids);
}
