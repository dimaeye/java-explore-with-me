package ru.practicum.explorewithme.ewmservice.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.ewmservice.error.exception.ConflictException;
import ru.practicum.explorewithme.ewmservice.error.exception.NotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {ValidationException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationAndIllegalArgExceptions(RuntimeException e) {
        log.error("Get ValidationAndIllegalArgExceptions {}", e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                "Bad params",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Get MethodArgumentNotValidException {}", e.getMessage(), e);
        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errors.toString(),
                "Bad params",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.warn("Get NotFoundException {}", e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                "Entity not found",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException e) {
        log.error("Get ConflictException {}", e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.CONFLICT,
                e.getMessage(),
                "Entity change conflict",
                LocalDateTime.now()
        );
    }

}
