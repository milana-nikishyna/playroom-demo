package by.gsu.olaksen.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RoomBookingSlot Model Tests")
class RoomBookingSlotTest {

    @Test
    @DisplayName("Should create RoomBookingSlot with free status")
    void shouldCreateRoomBookingSlotWithFreeStatus() {
        // Given
        String hour = "10:00";
        String status = "Свободно";

        // When
        RoomBookingSlot slot = new RoomBookingSlot(hour, status);

        // Then
        assertThat(slot).isNotNull();
        assertThat(slot.getHour()).isEqualTo(hour);
        assertThat(slot.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should create RoomBookingSlot with booked status")
    void shouldCreateRoomBookingSlotWithBookedStatus() {
        // Given
        String hour = "14:00";
        String status = "Забронировано";

        // When
        RoomBookingSlot slot = new RoomBookingSlot(hour, status);

        // Then
        assertThat(slot).isNotNull();
        assertThat(slot.getHour()).isEqualTo(hour);
        assertThat(slot.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should update status using setter")
    void shouldUpdateStatusUsingSetter() {
        // Given
        RoomBookingSlot slot = new RoomBookingSlot("12:00", "Свободно");

        // When
        slot.setStatus("Забронировано");

        // Then
        assertThat(slot.getStatus()).isEqualTo("Забронировано");
    }

    @Test
    @DisplayName("Hour field should be final and immutable")
    void hourFieldShouldBeFinalAndImmutable() {
        // Given
        String originalHour = "15:00";
        RoomBookingSlot slot = new RoomBookingSlot(originalHour, "Свободно");

        // Then
        assertThat(slot.getHour()).isEqualTo(originalHour);
        // Hour cannot be changed (no setter exists due to final)
    }

    @Test
    @DisplayName("Should handle different time formats")
    void shouldHandleDifferentTimeFormats() {
        // Given & When
        RoomBookingSlot slot1 = new RoomBookingSlot("09:00", "Свободно");
        RoomBookingSlot slot2 = new RoomBookingSlot("18:30", "Свободно");
        RoomBookingSlot slot3 = new RoomBookingSlot("23:59", "Свободно");

        // Then
        assertThat(slot1.getHour()).isEqualTo("09:00");
        assertThat(slot2.getHour()).isEqualTo("18:30");
        assertThat(slot3.getHour()).isEqualTo("23:59");
    }

    @Test
    @DisplayName("Should change status from free to booked")
    void shouldChangeStatusFromFreeToBooked() {
        // Given
        RoomBookingSlot slot = new RoomBookingSlot("16:00", "Свободно");

        // When
        slot.setStatus("Забронировано");

        // Then
        assertThat(slot.getStatus()).isEqualTo("Забронировано");
    }

    @Test
    @DisplayName("Should change status from booked to free")
    void shouldChangeStatusFromBookedToFree() {
        // Given
        RoomBookingSlot slot = new RoomBookingSlot("17:00", "Забронировано");

        // When
        slot.setStatus("Свободно");

        // Then
        assertThat(slot.getStatus()).isEqualTo("Свободно");
    }
}
