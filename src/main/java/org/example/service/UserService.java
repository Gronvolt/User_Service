package org.example.service;

import org.example.models.User;

import java.util.List;

public interface UserService {
   User createUser(User user);
   List<User> getAllUsers();
   User getUserInfo(Long id);
   User updateUser(User user);
   void deleteUser(Long userId);
}
