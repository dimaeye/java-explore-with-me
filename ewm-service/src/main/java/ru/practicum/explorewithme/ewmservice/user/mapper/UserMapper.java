package ru.practicum.explorewithme.ewmservice.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmservice.user.dto.UserDTO;
import ru.practicum.explorewithme.ewmservice.user.model.User;

@UtilityClass
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User toUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail()
        );
    }
}