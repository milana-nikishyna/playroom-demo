package by.gsu.olaksen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class RoomBookingSlot {
    private final String hour; // например, "10:00"
    @Setter private String status;     // "Свободно" или "Забронировано"
}
