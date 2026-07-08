package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(64) NOT NULL, " +
                "last_name VARCHAR(64) NOT NULL, " +
                "age TINYINT NOT NULL); ";
        executeSql(sql, "Не получилось создать таблицу");
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        executeSql(sql, "Не удалось удалить таблицу");
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";
        executeSql(sql, "Не удалось добавить пользователя", name, lastName, age);
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        executeSql(sql, "Не получилось удалить пользователя по id", id);
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();

                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setAge(resultSet.getByte("age"));

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не получилось получить таблицу всех user'ов", e);
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        executeSql(sql, "Не удалось удалить всех пользователей");
    }

    private void executeSql(String sql, String errorMessage, Object... params) {
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }
}
