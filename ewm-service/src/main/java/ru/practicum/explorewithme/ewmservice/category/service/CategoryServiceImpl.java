package ru.practicum.explorewithme.ewmservice.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmservice.category.exception.CategoryChangesException;
import ru.practicum.explorewithme.ewmservice.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.category.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Category addCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent())
            throw new CategoryChangesException("Категория с таким же именем уже создана");

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(int categoryId) {
        if (eventRepository.existsByCategoryId(categoryId))
            throw new CategoryChangesException("Существуют события, связанные с категорией");
        if (categoryRepository.findById(categoryId).isEmpty())
            throw new CategoryNotFoundException(categoryId);

        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public Category updateCategory(int categoryId, Category category) {
        Category currentCategory = getCategory(categoryId);

        if (categoryRepository.findByName(category.getName()).isPresent())
            throw new CategoryChangesException("Категория с таким же именем уже создана");

        currentCategory.setName(category.getName());

        return categoryRepository.save(currentCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategory(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
