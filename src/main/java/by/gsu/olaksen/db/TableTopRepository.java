package by.gsu.olaksen.db;

import by.gsu.olaksen.config.AppConfig;
import by.gsu.olaksen.model.TableTop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableTopRepository {
    private static final String URL = AppConfig.INSTANCE.getDbProperty("url");
    private static final String USER = AppConfig.INSTANCE.getDbProperty("user");
    private static final String PASSWORD = AppConfig.INSTANCE.getDbProperty("password");

    public TableTopRepository() {
        initDb();
    }

    private void initDb() {
        String sql = "CREATE TABLE IF NOT EXISTS tabletops (" +
                "id BIGINT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TableTop> getAllTableTops() {
        List<TableTop> result = new ArrayList<>();
        String sql = "SELECT id, name FROM tabletops";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new TableTop(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addTableTop(TableTop tabletop) {
        String sql = "INSERT INTO tabletops (id, name) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, tabletop.getTabletopId());
            ps.setString(2, tabletop.getTabletopName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTableTop(TableTop tabletop) {
        String sql = "UPDATE tabletops SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tabletop.getTabletopName());
            ps.setLong(2, tabletop.getTabletopId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTableTop(Long id) {
        String sql = "DELETE FROM tabletops WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
