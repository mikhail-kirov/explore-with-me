package ru.practicum.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.ApiError;
import ru.practicum.model.ErrorStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFound(final NotFoundException e) {
        return setApiError(e, ErrorStatus._404_NOT_FOUND, e.getReason());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryConflict(final IncorrectParameterException e) {
        return setApiError(e, ErrorStatus._409_CONFLICT, e.getReason());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUserObject(final BadRequestException e) {
        return setApiError(e, ErrorStatus._400_BAD_REQUEST, e.getReason());
    }

    private ApiError setApiError(final Exception e, ErrorStatus status, String reason) {
        return ApiError.builder()
                .message(e.getMessage())
                .reason(reason)
                .status(status.getDescription())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }

    private List<String> listErrors(Exception e) {
        List<String> errors = new ArrayList<>();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errors.add(sw.toString());
        return errors;
    }
}
