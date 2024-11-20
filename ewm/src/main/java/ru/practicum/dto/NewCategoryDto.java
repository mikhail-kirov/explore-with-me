package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class NewCategoryDto {

    @NotBlank
    @NotNull
    @Length(min = 1, max = 50)
    private String name;
}
