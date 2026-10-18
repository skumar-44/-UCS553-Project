package dao;

import db.Database;
import model.Tournament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TournamentDAO {

    public void addTournament(Tournament tournament) throws SQLException {

        String sql = """
                INSERT INTO tournaments (title, game, event_date, prize_pool)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tournament.getTitle());
            statement.setString(2, tournament.getGame());
            statement.setString(3, tournament.getEventDate());
            statement.setDouble(4, tournament.getPrizePool());

            statement.executeUpdate();
        }
    }

    public List<Tournament> getAllTournaments() throws SQLException {

        List<Tournament> tournaments = new ArrayList<>();

        String sql = """
                SELECT id, title, game, event_date, prize_pool
                FROM tournaments
                ORDER BY id
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Tournament tournament = new Tournament(
                        result.getInt("id"),
                        result.getString("title"),
                        result.getString("game"),
                        result.getString("event_date"),
                        result.getDouble("prize_pool"));

                tournaments.add(tournament);
            }
        }

        return tournaments;
    }

    public void updateTournament(Tournament tournament) throws SQLException {

        String sql = """
                UPDATE tournaments
                SET title = ?, game = ?, event_date = ?, prize_pool = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tournament.getTitle());
            statement.setString(2, tournament.getGame());
            statement.setString(3, tournament.getEventDate());
            statement.setDouble(4, tournament.getPrizePool());
            statement.setInt(5, tournament.getId());

            statement.executeUpdate();
        }
    }

    public void deleteTournament(int id) throws SQLException {

        String sql = "DELETE FROM tournaments WHERE id = ?";

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }
}
