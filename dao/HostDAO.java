
package dao;

import db.Database;
import model.Host;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HostDAO {

    public void addHost(Host host) throws SQLException {

        String sql = """
                INSERT INTO hosts (name, email, organization, contact_number)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, host.getName());
            statement.setString(2, host.getEmail());
            statement.setString(3, host.getOrganization());
            statement.setString(4, host.getContactNumber());

            statement.executeUpdate();
        }
    }

    public List<Host> getAllHosts() throws SQLException {

        List<Host> hosts = new ArrayList<>();

        String sql = """
                SELECT id, name, email, organization, contact_number
                FROM hosts
                ORDER BY id
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Host host = new Host(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("organization"),
                        result.getString("contact_number"));

                hosts.add(host);
            }
        }

        return hosts;
    }

    public void updateHost(Host host) throws SQLException {

        String sql = """
                UPDATE hosts
                SET name = ?, email = ?, organization = ?, contact_number = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, host.getName());
            statement.setString(2, host.getEmail());
            statement.setString(3, host.getOrganization());
            statement.setString(4, host.getContactNumber());
            statement.setInt(5, host.getId());

            statement.executeUpdate();
        }
    }

    public void deleteHost(int id) throws SQLException {

        String sql = "DELETE FROM hosts WHERE id = ?";

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
