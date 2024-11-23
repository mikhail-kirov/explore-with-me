package ru.practicum.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class ApiError {
    private List<String> errors;
    private String reason;
    private String message;
    private String status;
    private String timestamp;
}
