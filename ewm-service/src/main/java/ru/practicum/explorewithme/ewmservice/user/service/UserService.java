package ru.practicum.explorewithme.ewmservice.user.service;

import ru.practicum.explorewithme.ewmservice.user.exception.UserConflictException;
import ru.practicum.explorewithme.ewmservice.user.model.User;

import java.util.List;

public interface UserService {
    /**
     * @throws UserConflictException if user with the same name already exists
     */
    User createUser(User user);

    List<User> getAllUsersByIds(List<Integer> userIds, int from, int size);

    void deleteUser(int userId);
}
