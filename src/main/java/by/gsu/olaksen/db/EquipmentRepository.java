package by.gsu.olaksen.db;

import by.gsu.olaksen.model.Equipment;

import java.math.BigDecimal;
import java.util.List;

public class EquipmentRepository extends BaseRepository<Equipment> {

    @Override
    protected void initDb() {
        var sql = """
                CREATE TABLE IF NOT EXISTS equipment (
                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                    model VARCHAR(255) NOT NULL,
                    available BOOL NOT NULL,
                    notes VARCHAR(255),
                    type VARCHAR(50) NOT NULL,
                    price_per_hour VARCHAR(50) DEFAULT 0
                )
                """;
        executeStatement(sql);
    }

    public int add(Equipment equipment) {
        var sql = "INSERT INTO equipment (model, available, notes, type, price_per_hour) VALUES (?, ?, ?, ?, ?)";
        var isAvailable = "Свободно".equals(equipment.getStatus());
        return executeInsert(sql, ps -> {
            ps.setString(1, equipment.getModel());
            ps.setBoolean(2, isAvailable);
            ps.setString(3, equipment.getTerm());
            ps.setString(4, equipment.getType());
            ps.setString(5, equipment.getPricePerHour().toString());
        });
    }

    public List<Equipment> getAll() {
        var sql = "SELECT id, model, available, notes, type, price_per_hour FROM equipment";
        return executeQuery(sql, rs -> {
            var available = rs.getBoolean("available");
            var status = available ? "Свободно" : "В аренде";
            return new Equipment(
                    rs.getInt("id"),
                    rs.getString("model"),
                    status,
                    rs.getString("notes"),
                    rs.getString("type"),
                    new BigDecimal(rs.getString("price_per_hour"))
            );
        });
    }

    public List<Equipment> getByType(String type) {
        var sql = "SELECT id, model, available, notes, type, price_per_hour FROM equipment WHERE type = ?";
        return executeQuery(sql,
                ps -> ps.setString(1, type),
                rs -> {
                    var available = rs.getBoolean("available");
                    var status = available ? "Свободно" : "В аренде";
                    return new Equipment(
                            rs.getInt("id"),
                            rs.getString("model"),
                            status,
                            rs.getString("notes"),
                            rs.getString("type"),
                            new BigDecimal(rs.getString("price_per_hour"))
                    );
                });
    }

    public void update(Equipment equipment) {
        var sql = "UPDATE equipment SET model = ?, available = ?, notes = ?, type = ?, price_per_hour = ? WHERE id = ?";
        var isAvailable = "Свободно".equals(equipment.getStatus());
        executeUpdate(sql, ps -> {
            ps.setString(1, equipment.getModel());
            ps.setBoolean(2, isAvailable);
            ps.setString(3, equipment.getTerm());
            ps.setString(4, equipment.getType());
            ps.setString(5, equipment.getPricePerHour().toString());
            ps.setInt(6, equipment.getId());
        });
    }

    public void delete(int id) {
        var sql = "DELETE FROM equipment WHERE id = ?";
        executeDelete(sql, id);
    }

}
