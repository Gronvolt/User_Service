package org.example.util;

import jakarta.persistence.PersistenceException;
import org.example.dao.UserDaoImpl;
import org.example.models.User;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleInterface {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final UserService userService = new UserServiceImpl(new UserDaoImpl(HibernateUtil.getFactory()));

    public void runInterface() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> userService.createUser(readUser());
                    case "2" -> {
                        System.out.printf("%-4s %-15s %-15s %-12s%n", "ID", "Имя", "Фамилия", "Дата рожд.");
                        System.out.println("-".repeat(50));
                        userService.getAllUsers().forEach(this::printUser);
                    }
                    case "3" -> {
                        User user = readUserById();
                        if (user != null) {
                            System.out.println("Найден пользователь:");
                            System.out.printf("  ID:       %d%n  Имя:      %s%n  Фамилия:  %s%n  Дата рождения:     %s%n",
                                    user.getId(), user.getName(), user.getLastname(), user.getBirthday());
                        }
                    }
                    case "4" -> {
                        User managed = readUserById();
                        if (managed != null) {
                            User updated = readUser();
                            managed.setName(updated.getName());
                            managed.setLastname(updated.getLastname());
                            managed.setBirthday(updated.getBirthday());
                            userService.updateUser(managed);
                        }
                    }
                    case "5" -> userService.deleteUser(readId());
                    case "0" -> running = false;
                    default -> System.out.println("Нет такой команды");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка: неверный формат даты, ожидается ГГГГ-ММ-ДД");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (PersistenceException e) {
                System.out.println("Ошибка базы данных: " + e.getMessage());
            }
        }
    }

    private long readId() {
        System.out.println("Введите ID искомого пользователя из БД:");
        long id = scanner.nextLong();
        scanner.nextLine(); // потребляем перевод строки
        return id;
    }

    private User readUserById() {
        long id = readId();
        User user = userService.getUserInfo(id);
        if (user == null) {
            System.out.println("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    private User readUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите фамилию: ");
        String lastname = scanner.nextLine();
        System.out.print("Введите дату рождения (ГГГГ-ММ-ДД): ");
        LocalDate birthday = LocalDate.parse(scanner.nextLine(), DATE_FORMAT);
        return new User(name, lastname, birthday);
    }

    public static void printMenu() {
        System.out.println("\n===== МЕНЮ =====");
        System.out.println("1. Создать нового пользователя.");
        System.out.println("2. Посмотреть список пользователей.");
        System.out.println("3. Найти конкретного пользователя по ID.");
        System.out.println("4. Изменить данные о пользователе.");
        System.out.println("5. Удалить пользователя.");
        System.out.println("0. Выход.");
        System.out.print("> ");
    }


    private void printUser(User u) {
        System.out.printf("%-4s %-15s %-15s %-12s%n",
                u.getId(), u.getName(), u.getLastname(), u.getBirthday());
    }

}
