package ru.practicum.explorewithme.ewmservice.event.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.NotFoundException;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(int eventId) {
        super("Событие с id=" + eventId + " не найдено");
    }

    public EventNotFoundException(int eventId, int userId) {
        super("Событие с id=" + eventId + " у пользователя id=" + userId + " не найдено");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
