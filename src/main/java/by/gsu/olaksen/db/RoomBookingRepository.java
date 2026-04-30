package by.gsu.olaksen.db;

import java.time.LocalDate;
import java.util.List;

public class RoomBookingRepository extends BaseRepository<String> {

    @Override
    protected void initDb() {
        var sql = SQL_DIALECT.createRoomBookingTable();
        executeStatement(sql);
    }

    /**
     * Возвращает только забронированные слоты (их часы) для комнаты и даты.
     */
    public List<String> getBookedHours(int roomNumber, LocalDate date) {
        var sql = "SELECT hour FROM room_booking WHERE room_number = ? AND booking_date = ?";
        return executeQuery(sql,
                ps -> {
                    ps.setInt(1, roomNumber);
                    ps.setString(2, date.toString()); // ISO-формат yyyy-MM-dd
                },
                rs -> rs.getString("hour"));
    }

    /**
     * Отмечает слот как забронированный (создаёт запись, если её ещё нет).
     */
    public void bookSlot(int roomNumber, LocalDate date, String hour) {
        var sql = "INSERT INTO room_booking (room_number, booking_date, hour, status) VALUES (?, ?, ?, ?)";
        executeInsert(sql, ps -> {
            ps.setInt(1, roomNumber);
            ps.setString(2, date.toString());
            ps.setString(3, hour);
            ps.setString(4, "Забронировано");
        });
    }

    /**
     * Отменяет бронь слота (удаляет запись из БД).
     */
    public void cancelSlot(int roomNumber, LocalDate date, String hour) {
        var sql = "DELETE FROM room_booking WHERE room_number = ? AND booking_date = ? AND hour = ?";
        executeDelete(sql, ps -> {
            ps.setInt(1, roomNumber);
            ps.setString(2, date.toString());
            ps.setString(3, hour);
        });
    }
}


