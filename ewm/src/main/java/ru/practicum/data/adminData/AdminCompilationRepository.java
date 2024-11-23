package ru.practicum.data.adminData;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Compilation;

public interface AdminCompilationRepository extends JpaRepository<Compilation, Long> {
}
