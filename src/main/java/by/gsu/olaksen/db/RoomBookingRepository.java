package by.gsu.olaksen.db;

import by.gsu.olaksen.config.AppConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomBookingRepository {

    private static final String URL = AppConfig.getInstance().getDbProperty("url");
    private static final String USER = AppConfig.getInstance().getDbProperty("user");
    private static final String PASSWORD = AppConfig.getInstance().getDbProperty("password");

    public RoomBookingRepository() {
        initDb();
    }

    private void initDb() {
        String sql = """
                CREATE TABLE IF NOT EXISTS room_booking (
                    id INTEGER PRIMARY KEY NOT NULL,
                    room_number INTEGER NOT NULL,
                    booking_date TEXT NOT NULL,
                    hour VARCHAR(10) NOT NULL,
                    status VARCHAR(20) NOT NULL
                )
                """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает только забронированные слоты (их часы) для комнаты и даты.
     */
    public List<String> getBookedHours(int roomNumber, LocalDate date) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT hour FROM room_booking WHERE room_number = ? AND booking_date = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ps.setString(2, date.toString()); // ISO-формат yyyy-MM-dd
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("hour"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Отмечает слот как забронированный (создаёт запись, если её ещё нет).
     */
    public void bookSlot(int roomNumber, LocalDate date, String hour) {
        String sql = "INSERT INTO room_booking (room_number, booking_date, hour, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ps.setString(2, date.toString());
            ps.setString(3, hour);
            ps.setString(4, "Забронировано");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отменяет бронь слота (удаляет запись из БД).
     */
    public void cancelSlot(int roomNumber, LocalDate date, String hour) {
        String sql = "DELETE FROM room_booking WHERE room_number = ? AND booking_date = ? AND hour = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ps.setString(2, date.toString());
            ps.setString(3, hour);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


