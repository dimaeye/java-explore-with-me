package ru.practicum.explorewithme.ewmservice.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.category.dto.CategoryDTO;
import ru.practicum.explorewithme.ewmservice.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public static CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    public static Category toCategory(CategoryDTO categoryDTO) {
        return Category.builder().name(categoryDTO.getName()).build();
    }
}
