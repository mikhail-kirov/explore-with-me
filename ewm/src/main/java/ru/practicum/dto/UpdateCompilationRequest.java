package ru.practicum.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateCompilationRequest {
    private List<Long> events = null;
    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
