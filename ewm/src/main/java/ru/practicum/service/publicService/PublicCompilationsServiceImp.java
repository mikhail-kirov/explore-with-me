package ru.practicum.service.publicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.data.publicData.PublicCompilationsRepository;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.mapper.MappingCompilation;
import ru.practicum.mapper.MappingEvent;
import ru.practicum.model.Compilation;
import ru.practicum.validation.ValidCompilation;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCompilationsServiceImp implements PublicCompilationsService {

    private final PublicCompilationsRepository publicCompilationsRepository;
    private final ValidCompilation validCompilation;

    @Override
    public List<CompilationDto> getCompilationDto(Boolean pinned, Integer from, Integer size) {

        List<Compilation> compilations;
        if (pinned != null) {
            compilations = publicCompilationsRepository.findAllCompilation(pinned, from, size);
        } else {
            compilations = publicCompilationsRepository.findAllCompilationPage(from, size);
        }
        if (!compilations.isEmpty()) {
            log.info("Подборка событий отправлена");
            return compilations.stream()
                    .map(com -> MappingCompilation.toCompilationDto(com, MappingEvent.toEventShortDto(com.getEvents())))
                    .toList();
        }
        log.info("Список подборки событий по заданным параметрам пуст");
        return List.of();
    }

    @Override
    public CompilationDto getCompilationDtoById(Long compId) {
        Compilation compilation = validCompilation.validCompilationById(compId);
        List<EventShortDto> eventShortDto = MappingEvent.toEventShortDto(compilation.getEvents());
        log.info("Подборка событий по '{}' отправлена", compilation.getTitle());
        return MappingCompilation.toCompilationDto(compilation, eventShortDto);
    }
}
