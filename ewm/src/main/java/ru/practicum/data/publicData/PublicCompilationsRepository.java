package ru.practicum.data.publicData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Compilation;

import java.util.List;

public interface PublicCompilationsRepository extends JpaRepository<Compilation, Long> {

    @Query("select com " +
            "from Compilation as com " +
            "where com.pinned = ?1 " +
            "order by com.id asc " +
            "limit ?3 offset ?2")
    List<Compilation> findAllCompilation(Boolean pinned, Integer from, Integer size);

    @Query("select com " +
            "from Compilation as com " +
            "order by com.id asc " +
            "limit ?2 offset ?1")
    List<Compilation> findAllCompilationPage(Integer from, Integer size);
}
