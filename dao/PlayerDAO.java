package dao;

import db.Database;
import model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    // Add a player
    public void addPlayer(Player player) throws SQLException {
        String sql = """
                INSERT INTO players (name, email, game_id)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, player.getName());
            statement.setString(2, player.getEmail());
            statement.setString(3, player.getGameId());

            statement.executeUpdate();
        }
    }

    public List<Player> getAllPlayers() throws SQLException {
        List<Player> players = new ArrayList<>();

        String sql = """
                SELECT id, name, email, game_id
                FROM players
                ORDER BY id
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Player player = new Player(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("game_id"));

                players.add(player);
            }
        }

        return players;
    }

    public void updatePlayer(Player player) throws SQLException {
        String sql = """
                UPDATE players
                SET name = ?, email = ?, game_id = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, player.getName());
            statement.setString(2, player.getEmail());
            statement.setString(3, player.getGameId());
            statement.setInt(4, player.getId());

            statement.executeUpdate();
        }
    }

    public void deletePlayer(int id) throws SQLException {
        String sql = "DELETE FROM players WHERE id = ?";

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
