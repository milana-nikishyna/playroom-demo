package by.gsu.olaksen.model;

public class RoomBookingSlot {
    private final String hour; // например, "10:00"
    private String status;     // "Свободно" или "Забронировано"

    public RoomBookingSlot(String hour, String status) {
        this.hour = hour;
        this.status = status;
    }

    public String getHour() { return hour; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
