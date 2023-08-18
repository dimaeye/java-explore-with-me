package ru.practicum.explorewithme.ewmservice.category.service;

import ru.practicum.explorewithme.ewmservice.category.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);

    void deleteCategory(int categoryId);

    Category updateCategory(int categoryId, Category category);

    List<Category> getAllCategories(int from, int size);

    Category getCategory(int categoryId);
}
