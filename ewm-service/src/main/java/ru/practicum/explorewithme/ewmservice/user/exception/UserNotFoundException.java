package ru.practicum.explorewithme.ewmservice.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(int userId) {
        super("Пользователь с id=" + userId + " не найден!");
    }
}