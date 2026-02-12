package by.gsu.olaksen.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RoomBookingSlot {
    private final String hour; // например, "10:00"
    @Setter private String status;     // "Свободно" или "Забронировано"

    public RoomBookingSlot(String hour, String status) {
        this.hour = hour;
        this.status = status;
    }
}
