package ru.practicum.exeption;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final String reason;
    private final String message;

    public BadRequestException(final String message, final String reason) {
        this.message = message;
        this.reason = reason;
    }
}
