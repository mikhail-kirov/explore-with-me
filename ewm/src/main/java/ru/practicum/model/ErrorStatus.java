package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorStatus {
    _200_OK("200 OK"),
    _201_CREATED("201 CREATED"),
    _400_BAD_REQUEST("400 BAD_REQUEST"),
    _404_NOT_FOUND("404 NOT_FOUND"),
    _409_CONFLICT("409 CONFLICT"),
    _500_INTERNAL_SERVER_ERROR("500 INTERNAL_SERVER_ERROR");

    private final String description;

    ErrorStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.valueOf(description);
    }

    public static ErrorStatus fromDescription(String description) {
        for (ErrorStatus errorStatus : ErrorStatus.values()) {
            if (errorStatus.description.equals(description)) {
                return errorStatus;
            }
        }
        throw new IllegalArgumentException("Статус " + description + "не предусмотрен");
    }
}
