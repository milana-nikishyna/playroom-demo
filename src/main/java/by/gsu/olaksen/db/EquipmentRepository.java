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
                "notes VARCHAR(255) " +
                ")";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int add(Equipment equipment) {
        String sql = "INSERT INTO equipment (model, available, notes) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    var status = equipment.getStatus();
                    boolean isAvailable;
                    switch (status) {
                        case "Свободно" -> isAvailable = true;
                        default -> isAvailable = false;
                    }
            ps.setString(1, equipment.getModel());
            ps.setBoolean(2, isAvailable);
            ps.setString(3, equipment.getTerm());
            


            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

        public List<Equipment> getAll() {
        List<Equipment> result = new ArrayList<>();
        String sql = "SELECT id, model, available, notes FROM equipment";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Equipment(rs.getInt("id"), rs.getString("model"), String.valueOf(rs.getBoolean("available")), rs.getString("notes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
