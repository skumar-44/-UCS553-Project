
package dao;

import db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.AuthUser;

public class AuthDAO {

    public void register(String name, String email, String password)
            throws SQLException {

        String sql = """
                INSERT INTO users (name, email, password)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name.trim());
            statement.setString(2, email.trim());
            statement.setString(3, password);

            statement.executeUpdate();
        }
    }

    public AuthUser signIn(String email, String password)
            throws SQLException {

        String sql = """
                SELECT id, name, email, password
                FROM users
                WHERE email = ? COLLATE NOCASE
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.trim());

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    String savedPassword = result.getString("password");

                    if (savedPassword.equals(password)) {
                        return new AuthUser(
                                result.getInt("id"),
                                result.getString("name"),
                                result.getString("email"));
                    }
                }
            }
        }
        return null;
    }
}
