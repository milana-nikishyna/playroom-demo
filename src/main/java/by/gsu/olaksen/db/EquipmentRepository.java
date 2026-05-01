package by.gsu.olaksen.db;

import by.gsu.olaksen.model.Equipment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class EquipmentRepository extends BaseRepository<Equipment> {

    private static String toIso(LocalDateTime dt) {
        return dt == null ? null : dt.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private static LocalDateTime fromIso(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return LocalDateTime.parse(s, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void initDb() {
        executeStatement(SQL_DIALECT.createEquipmentTable());
    }

    public int add(Equipment equipment) {
        var sql = "INSERT INTO equipment (model, available, rent_until, type, price_per_hour) VALUES (?, ?, ?, ?, ?)";
        var isAvailable = "Свободно".equals(equipment.getStatus());
        return executeInsert(sql, ps -> {
            ps.setString(1, equipment.getModel());
            ps.setBoolean(2, isAvailable);
            ps.setString(3, toIso(equipment.getRentUntil()));
            ps.setString(4, equipment.getType());
            ps.setString(5, equipment.getPricePerHour().toString());
        });
    }

    public List<Equipment> getAll() {
        var sql = "SELECT id, model, available, rent_until, type, price_per_hour FROM equipment";
        return executeQuery(sql, rs -> {
            var available = rs.getBoolean("available");
            var status = available ? "Свободно" : "В аренде";
            return new Equipment(
                    rs.getInt("id"),
                    rs.getString("model"),
                    status,
                    fromIso(rs.getString("rent_until")),
                    rs.getString("type"),
                    new BigDecimal(rs.getString("price_per_hour"))
            );
        });
    }

    public void delete(int id) {
        var sql = "DELETE FROM equipment WHERE id = ?";
        executeDelete(sql, id);
    }

    public List<Equipment> getByType(String type) {
        var sql = "SELECT id, model, available, rent_until, type, price_per_hour FROM equipment WHERE type = ?";
        return executeQuery(sql,
                ps -> ps.setString(1, type),
                rs -> {
                    var available = rs.getBoolean("available");
                    var status = available ? "Свободно" : "В аренде";
                    return new Equipment(
                            rs.getInt("id"),
                            rs.getString("model"),
                            status,
                            fromIso(rs.getString("rent_until")),
                            rs.getString("type"),
                            new BigDecimal(rs.getString("price_per_hour"))
                    );
                });
    }

    public void update(Equipment equipment) {
        var sql = "UPDATE equipment SET model = ?, available = ?, rent_until = ?, type = ?, price_per_hour = ? WHERE id = ?";
        var isAvailable = "Свободно".equals(equipment.getStatus());
        executeUpdate(sql, ps -> {
            ps.setString(1, equipment.getModel());
            ps.setBoolean(2, isAvailable);
            ps.setString(3, toIso(equipment.getRentUntil()));
            ps.setString(4, equipment.getType());
            ps.setString(5, equipment.getPricePerHour().toString());
            ps.setInt(6, equipment.getId());
        });
    }
}
