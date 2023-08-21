package ru.practicum.explorewithme.ewmservice.request.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.NotFoundException;

public class RequestNotFoundException extends NotFoundException {

    public RequestNotFoundException(int requestId) {
        super("Запрос с id=" + requestId + " не найден!");
    }
}
