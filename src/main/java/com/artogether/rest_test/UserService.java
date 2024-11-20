package com.artogether.rest_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    User getUser(int id) {
        return userRepository.getUser(id);
    }

    List<User> getUsers() {
        return userRepository.getAllUsers();
    }

    void addUser(User user) {
        userRepository.addUser(user);
    }

    void updateUser(User user) {
        userRepository.updateUser(user);
    }

    void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
