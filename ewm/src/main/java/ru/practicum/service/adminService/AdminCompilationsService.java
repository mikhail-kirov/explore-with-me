package ru.practicum.service.adminService;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;

public interface AdminCompilationsService {

    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    ResponseEntity<Void> deleteCompilation(Long compId);

    CompilationDto patchCompilation(Long compId, NewCompilationDto newCompilationDto);
}
