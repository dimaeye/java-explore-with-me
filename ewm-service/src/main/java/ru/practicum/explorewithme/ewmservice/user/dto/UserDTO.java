package ru.practicum.explorewithme.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    //    @JsonIgnore
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 250)
    private String name;
    @NotNull
    @Size(min = 6, max = 254)
    @Email(message = "Передан некорректный e-mail")
    private String email;
}