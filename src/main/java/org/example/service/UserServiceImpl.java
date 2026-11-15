package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.models.User;
import java.util.List;
import java.util.NoSuchElementException;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User user) {
        return userDao.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User getUserInfo(Long id) {
        return userDao.findById(id).orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public User updateUser(User user) {
        return userDao.update(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User managed = userDao.findById(userId).orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + userId + " не найден"));
        userDao.delete(managed);
    }
}

