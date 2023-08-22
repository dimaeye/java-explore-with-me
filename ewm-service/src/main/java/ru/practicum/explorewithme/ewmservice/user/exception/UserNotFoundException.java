package ru.practicum.explorewithme.ewmservice.user.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(int userId) {
        super("Пользователь с id=" + userId + " не найден!");
    }
}