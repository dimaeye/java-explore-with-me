package ru.practicum.explorewithme.ewmservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmservice.user.exception.UserConflictException;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent())
            throw new UserConflictException("Пользователь с именем " + user.getName() + " уже существует");
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsersByIds(List<Integer> userIds, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (userIds == null || userIds.isEmpty())
            return userRepository.findAll(pageable).getContent();
        else
            return userRepository.findAllByIdIn(userIds, pageable).getContent();
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
