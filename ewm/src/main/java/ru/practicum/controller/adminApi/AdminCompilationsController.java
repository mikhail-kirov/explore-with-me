package ru.practicum.controller.adminApi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.service.adminService.AdminCompilationsService;


@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationsController {

    private final AdminCompilationsService adminCompilationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Admin-запрос на добавление новой подборки: {}", newCompilationDto.getTitle());
        return adminCompilationsService.postCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable Long compId,
                                           @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Admin-запрос на изменение подборки с ID {}", compId);
        return adminCompilationsService.patchCompilation(compId, newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<?> deleteCompilation(@PathVariable Long compId) {
        log.info("Admin-запрос на удаление подборки с ID {}", compId);
        return adminCompilationsService.deleteCompilation(compId);
    }
}
