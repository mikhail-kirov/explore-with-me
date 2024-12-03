package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewCompilationDto {
    private List<Long> events = new ArrayList<>();
    private Boolean pinned = Boolean.FALSE;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
