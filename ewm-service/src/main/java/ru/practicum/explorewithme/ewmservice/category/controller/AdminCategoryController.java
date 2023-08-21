package ru.practicum.explorewithme.ewmservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.category.dto.CategoryDTO;
import ru.practicum.explorewithme.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@Validated
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        Category category = categoryService.addCategory(CategoryMapper.toCategory(categoryDTO));

        return CategoryMapper.toCategoryDTO(category);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDTO updateCategory(@PathVariable Integer categoryId, @RequestBody @Valid CategoryDTO categoryDTO) {
        Category category = categoryService.updateCategory(categoryId, CategoryMapper.toCategory(categoryDTO));

        return CategoryMapper.toCategoryDTO(category);
    }

}
