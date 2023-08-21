package ru.practicum.explorewithme.ewmservice.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class CategoryDTO {
    int id;
    @NotBlank
    @Size(min = 1, max = 50)
    String name;
}
