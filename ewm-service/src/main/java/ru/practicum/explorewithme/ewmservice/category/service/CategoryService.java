package ru.practicum.explorewithme.ewmservice.category.service;

import ru.practicum.explorewithme.ewmservice.category.exception.CategoryChangesException;
import ru.practicum.explorewithme.ewmservice.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.ewmservice.category.model.Category;

import java.util.List;

public interface CategoryService {
    /**
     * @throws CategoryChangesException when category with the same name has already existed
     */
    Category addCategory(Category category);

    /**
     * @throws CategoryChangesException  when there are events in this category
     * @throws CategoryNotFoundException when the category is not found
     */
    void deleteCategory(int categoryId);

    /**
     * @throws CategoryChangesException when category with the same name has already existed
     */
    Category updateCategory(int categoryId, Category category);

    List<Category> getAllCategories(int from, int size);

    /**
     * @throws CategoryNotFoundException when the category is not found
     */
    Category getCategory(int categoryId);
}
