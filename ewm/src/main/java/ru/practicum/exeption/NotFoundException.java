package ru.practicum.exeption;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String reason;
    private final String message;

    public NotFoundException(final String message, final String reason) {
        this.message = message;
        this.reason = reason;
    }
}
