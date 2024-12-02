package ru.practicum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto extends User {

    @Email
    @NotNull
    private String email;

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String country;
}
