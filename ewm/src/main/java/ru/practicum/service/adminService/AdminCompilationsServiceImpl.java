package ru.practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.adminData.AdminCompilationRepository;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.mapper.MappingCompilation;
import ru.practicum.mapper.MappingEvent;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.validation.ValidCompilation;
import ru.practicum.validation.ValidEvent;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {

    private final AdminCompilationRepository adminCompilationRepository;
    private final ValidEvent validEvent;
    private final ValidCompilation validCompilation;

    @Override
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = newCompilationDto.getEvents().stream().map(validEvent::validEventById).toList();
        Compilation compilation = adminCompilationRepository.save(MappingCompilation.toCompilation(newCompilationDto, events));
        log.info("Подборка событий сохранена: {}", compilation.getTitle());
        List<EventShortDto> eventShortDto = MappingEvent.toEventShortDto(compilation.getEvents());
        return MappingCompilation.toCompilationDto(compilation, eventShortDto);
    }

    @Override
    public ResponseEntity<Void> deleteCompilation(Long compId) {
        Compilation compilation = validCompilation.validCompilationById(compId);
        adminCompilationRepository.deleteById(compId);
        log.info("Подборка событий удалена: {}", compilation.getTitle());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public CompilationDto patchCompilation(Long compId, NewCompilationDto newCompilationDto) {
        validCompilation.validCompilationByLengthTitle(newCompilationDto.getTitle());
        Compilation compilation = validCompilation.validCompilationById(compId);
        compilation.setEvents(!newCompilationDto.getEvents().isEmpty() ?
                newCompilationDto.getEvents().stream().map(validEvent::validEventById).toList() : compilation.getEvents());
        compilation.setPinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : compilation.getPinned());
        compilation.setTitle(newCompilationDto.getTitle() != null ? newCompilationDto.getTitle() : compilation.getTitle());
        adminCompilationRepository.flush();
        log.info("Подборка событий после изменений сохранена: {}", compilation.getTitle());
        List<EventShortDto> eventShortDto = MappingEvent.toEventShortDto(compilation.getEvents());
        return MappingCompilation.toCompilationDto(compilation, eventShortDto);
    }
}
