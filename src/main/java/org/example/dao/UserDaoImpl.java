package org.example.dao;

import org.example.models.User;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserDaoImpl implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User user) {
        return executeInTransaction(session -> {
            session.persist(user);
            log.info("Создан новый пользователь: {} {}. Время создания: {}", user.getName(), user.getLastname(), user.getCreatedAt());
            return user;
        });
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.of(session.find(User.class, id));
        } catch (RuntimeException e) {
            log.error("Ошибка БД. Не удается получить пользователя.........");
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public User update(User user) {
        return executeInTransaction(session -> {
            User merged = session.merge(user);
            log.info("Информация о пользователе {} обновлена", merged.getId());
            return merged;
        });
    }

    @Override
    public void delete(User user) {
        executeInTransaction(session -> {
            User managed = session.contains(user) ? user : session.merge(user);
            session.remove(managed);
            return null;
        });
        log.info("Пользователь {} удален", user.getId());
    }


    private <T> T executeInTransaction(SessionAction<T> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T result = action.apply(session);
                tx.commit();
                return result;
            } catch (RuntimeException e) {
                tx.rollback();
                log.error("Ошибка БД. Откатываю транзакцию.........");
                throw e;
            }
        }
    }

    @FunctionalInterface
    private interface SessionAction<T> {
        T apply(Session session);
    }
}
