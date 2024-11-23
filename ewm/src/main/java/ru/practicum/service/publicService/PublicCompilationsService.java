package ru.practicum.service.publicService;

import ru.practicum.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {

    List<CompilationDto> getCompilationDto(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationDtoById(Long compId);
}
