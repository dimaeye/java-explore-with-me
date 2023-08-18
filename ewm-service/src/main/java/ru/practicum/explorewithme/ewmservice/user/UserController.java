package ru.practicum.explorewithme.ewmservice.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.user.dto.UserDTO;
import ru.practicum.explorewithme.ewmservice.user.mapper.UserMapper;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("Добавление пользователя - " + userDTO);
        User createdUser = userService.createUser(UserMapper.toUser(userDTO));
        log.info("Пользователь добавлен успешно - " + createdUser.getId());
        return UserMapper.toUserDTO(createdUser);
    }

    @GetMapping
    public List<UserDTO> getAllUsers(
            @RequestParam(defaultValue = "") List<Integer> ids,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        List<User> users = userService.getAllUsersByIds(ids, from, size);

        return users.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Удаление пользователя - " + userId);
        userService.deleteUser(userId);
        log.info("Пользователь удален успешно");
    }
}
