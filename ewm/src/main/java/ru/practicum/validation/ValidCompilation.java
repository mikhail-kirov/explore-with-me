package ru.practicum.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.data.adminData.AdminCompilationRepository;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.model.Compilation;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidCompilation {

    private final AdminCompilationRepository adminCompilationRepository;

    public Compilation validCompilationById(Long id) {
        return adminCompilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The compilation object was not found.","Compilation with id="+id+" was not found"));
    }

    public void validCompilationByLengthTitle(String name) {
        if (name != null && name.length() > 50) {
            throw new BadRequestException("The title length does not meet the condition", "Bad Request.");
        }
    }
}
