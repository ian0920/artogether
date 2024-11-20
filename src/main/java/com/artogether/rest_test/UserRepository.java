package com.artogether.rest_test;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserRepository {


    List<User> users = new ArrayList<User>(Arrays.asList(
            new User(1,10),
            new User(2,20),
            new User(3, 30),
            new User(4, 40),
            new User(5,50)
    ));

    User getUser(int id) {

        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }


    void addUser(User user) {
        users.add(user);
        System.out.println(users);
    }

    void updateUser(User user) {
        for (User user1 : users) {
            if (user1.getId() == user.getId()) {
                user1.setAge(user.getAge());
            }
        }

        System.out.println(users);

    }

    void deleteUser(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                users.remove(user);
            }
        }
        System.out.println(users);
    }

    List<User> getAllUsers() {
        return users;
    }

}
