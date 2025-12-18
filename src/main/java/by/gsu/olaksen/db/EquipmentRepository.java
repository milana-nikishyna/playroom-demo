package by.gsu.olaksen.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.gsu.olaksen.config.AppConfig;
import by.gsu.olaksen.model.Equipment;

public class EquipmentRepository {
    private static final String URL = AppConfig.getInstance().getDbProperty("url");
    private static final String USER = AppConfig.getInstance().getDbProperty("user");
    private static final String PASSWORD = AppConfig.getInstance().getDbProperty("password");

    public EquipmentRepository() {
        initDb();
    }

    private void initDb() {
        String sql = "CREATE TABLE IF NOT EXISTS equipment (" +
                "id INTEGER PRIMARY KEY NOT NULL, " +
                "model VARCHAR(255) NOT NULL, " +
                "available BOOL NOT NULL, " +
                "notes VARCHAR(255), " +
                "type VARCHAR(50) NOT NULL, " +
                "price_per_hour REAL DEFAULT 0 " +
                ")";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            // На случай уже существующей таблицы без колонки price_per_hour
            try {
                stmt.execute("ALTER TABLE equipment ADD COLUMN price_per_hour REAL DEFAULT 0");
            } catch (SQLException ignored) {
                // колонка уже есть
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int add(Equipment equipment) {
        String sql = "INSERT INTO equipment (model, available, notes, type, price_per_hour) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var status = equipment.getStatus();
            boolean isAvailable = "Свободно".equals(status);
            ps.setString(1, equipment.getModel());
            ps.setBoolean(2, isAvailable);
            ps.setString(3, equipment.getTerm());
            ps.setString(4, equipment.getType());
            ps.setDouble(5, equipment.getPricePerHour());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Equipment> getAll() {
        List<Equipment> result = new ArrayList<>();
        String sql = "SELECT id, model, available, notes, type, price_per_hour FROM equipment";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                boolean available = rs.getBoolean("available");
                String status = available ? "Свободно" : "В аренде";
                result.add(new Equipment(
                        rs.getInt("id"),
                        rs.getString("model"),
                        status,
                        rs.getString("notes"),
                        rs.getString("type"),
                        rs.getDouble("price_per_hour")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Equipment> getByType(String type) {
        List<Equipment> result = new ArrayList<>();
        String sql = "SELECT id, model, available, notes, type, price_per_hour FROM equipment WHERE type = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    boolean available = rs.getBoolean("available");
                    String status = available ? "Свободно" : "В аренде";
                    result.add(new Equipment(
                            rs.getInt("id"),
                            rs.getString("model"),
                            status,
                            rs.getString("notes"),
                            rs.getString("type"),
                            rs.getDouble("price_per_hour")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void update(Equipment equipment) {
        String sql = "UPDATE equipment SET model = ?, available = ?, notes = ?, type = ?, price_per_hour = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            boolean isAvailable = "Свободно".equals(equipment.getStatus());
            ps.setString(1, equipment.getModel());
            ps.setBoolean(2, isAvailable);
            ps.setString(3, equipment.getTerm());
            ps.setString(4, equipment.getType());
            ps.setDouble(5, equipment.getPricePerHour());
            ps.setInt(6, equipment.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM equipment WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
