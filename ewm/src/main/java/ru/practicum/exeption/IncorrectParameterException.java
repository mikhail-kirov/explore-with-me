package ru.practicum.exeption;

import lombok.Getter;

@Getter
public class IncorrectParameterException extends RuntimeException {

    private final String reason;
    private final String message;

    public IncorrectParameterException(final String message, final String reason) {
            this.message = message;
            this.reason = reason;
    }
}
