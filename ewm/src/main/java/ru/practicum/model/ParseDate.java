package ru.practicum.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exeption.IncorrectParameterException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class ParseDate {

    public LocalDateTime parse(String date) {
        if (date == null) {
            return LocalDateTime.now();
        } else {
            String decode = URLDecoder.decode(date, StandardCharsets.UTF_8);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            try {
                return LocalDateTime.parse(decode, formatter);
            } catch (DateTimeParseException e) {
                log.info("Дата {} передана в неверном формате", decode);
                throw new IncorrectParameterException("Дата " + decode + " передана в неверном формате", "Incorrectly made request.");
            }
        }
    }
}
