package ru.practicum.explorewithme.ewmservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.category.dto.CategoryDTO;
import ru.practicum.explorewithme.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmservice.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@Validated
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return categoryService.getAllCategories(from, size).stream()
                .map(CategoryMapper::toCategoryDTO).collect(Collectors.toList());
    }

    @GetMapping("/{categoryId}")
    public CategoryDTO getCategory(@PathVariable Integer categoryId) {
        return CategoryMapper.toCategoryDTO(categoryService.getCategory(categoryId));
    }
}
