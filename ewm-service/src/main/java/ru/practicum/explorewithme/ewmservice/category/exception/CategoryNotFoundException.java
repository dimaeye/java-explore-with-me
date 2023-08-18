package ru.practicum.explorewithme.ewmservice.category.exception;

import ru.practicum.explorewithme.ewmservice.error.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(int categoryId) {
        super("Категория с id=" + categoryId + " не найден!");
    }
}
