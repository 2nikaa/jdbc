package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        userService.saveUser("nikita", "nikonov", (byte) 24);
        userService.saveUser("vika", "sus", (byte) 21);
        userService.saveUser("elon", "musk", (byte) 99);
        userService.saveUser("vova", "putin", (byte) 3);

        userService.getAllUsers().forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
