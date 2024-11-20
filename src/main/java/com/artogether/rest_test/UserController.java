package com.artogether.rest_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("user={id}")
    public User user(@PathVariable("id") int id) {
        return userService.getUser(id);
    }

    @PostMapping("user")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @PutMapping("user")
    User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return userService.getUser(user.getId());
    }

    @DeleteMapping("user/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "User deleted";
    }

}
