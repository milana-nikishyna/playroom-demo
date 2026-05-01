package by.gsu.olaksen.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Equipment Model Tests")
class EquipmentTest {

    @Test
    @DisplayName("Should create Equipment with all fields")
    void shouldCreateEquipmentWithAllFields() {
        // Given
        int id = 1;
        String model = "Xbox Series X";
        String status = "Свободно";
        LocalDateTime rentUntil = LocalDateTime.of(2026, 5, 2, 12, 0);
        String type = "Console";
        var pricePerHour = new BigDecimal("50.5");

        // When
        Equipment equipment = new Equipment(id, model, status, rentUntil, type, pricePerHour);

        // Then
        assertThat(equipment).isNotNull();
        assertThat(equipment.getId()).isEqualTo(id);
        assertThat(equipment.getModel()).isEqualTo(model);
        assertThat(equipment.getStatus()).isEqualTo(status);
        assertThat(equipment.getRentUntil()).isEqualTo(rentUntil);
        assertThat(equipment.getType()).isEqualTo(type);
        assertThat(equipment.getPricePerHour()).isEqualTo(pricePerHour);
    }

    @Test
    @DisplayName("Should create Equipment without id")
    void shouldCreateEquipmentWithoutId() {
        // Given
        String model = "PlayStation 5";
        String status = "В аренде";
        LocalDateTime rentUntil = LocalDateTime.of(2026, 5, 2, 14, 30);
        String type = "Console";

        // When
        Equipment equipment = new Equipment(model, status, rentUntil, type);

        // Then
        assertThat(equipment).isNotNull();
        assertThat(equipment.getModel()).isEqualTo(model);
        assertThat(equipment.getStatus()).isEqualTo(status);
        assertThat(equipment.getRentUntil()).isEqualTo(rentUntil);
        assertThat(equipment.getType()).isEqualTo(type);
        assertThat(equipment.getId()).isZero(); // Default value
    }

    @Test
    @DisplayName("Should update Equipment fields using setters")
    void shouldUpdateEquipmentFieldsUsingSetters() {
        // Given
        Equipment equipment = new Equipment("Old Model", "Свободно", null, "Console");

        // When
        equipment.setId(5);
        equipment.setModel("New Model");
        equipment.setStatus("В аренде");
        equipment.setRentUntil(LocalDateTime.of(2026, 5, 3, 9, 0));
        equipment.setType("PC");
        equipment.setPricePerHour(new BigDecimal("75.5"));

        // Then
        assertThat(equipment.getId()).isEqualTo(5);
        assertThat(equipment.getModel()).isEqualTo("New Model");
        assertThat(equipment.getStatus()).isEqualTo("В аренде");
        assertThat(equipment.getRentUntil()).isEqualTo(LocalDateTime.of(2026, 5, 3, 9, 0));
        assertThat(equipment.getType()).isEqualTo("PC");
        assertThat(equipment.getPricePerHour()).isEqualTo(new BigDecimal("75.5"));
    }

    @Test
    @DisplayName("Should handle zero price per hour")
    void shouldHandleZeroPricePerHour() {
        // Given & When
        Equipment equipment = new Equipment(1, "Free Equipment", "Свободно", null, "Other", new BigDecimal("0.0"));

        // Then
        assertThat(equipment.getPricePerHour()).isZero();
    }

    @Test
    @DisplayName("Should handle negative price per hour")
    void shouldHandleNegativePricePerHour() {
        // Given & When
        Equipment equipment = new Equipment(1, "Equipment", "Свободно", null, "Other", new BigDecimal("-10.0"));

        // Then
        assertThat(equipment.getPricePerHour()).isNegative();
    }

    @Test
    @DisplayName("Should correctly handle different statuses")
    void shouldCorrectlyHandleDifferentStatuses() {
        // Given
        Equipment available = new Equipment("Model1", "Свободно", null, "Type");
        Equipment rented = new Equipment("Model2", "В аренде", LocalDateTime.of(2026, 5, 2, 12, 0), "Type");

        // Then
        assertThat(available.getStatus()).isEqualTo("Свободно");
        assertThat(rented.getStatus()).isEqualTo("В аренде");
    }

    @Test
    @DisplayName("Should correctly handle different types")
    void shouldCorrectlyHandleDifferentTypes() {
        // Given
        Equipment console = new Equipment("Xbox", "Свободно", null, "Console");
        Equipment pc = new Equipment("Gaming PC", "Свободно", null, "PC");
        Equipment vr = new Equipment("Oculus", "Свободно", null, "VR");

        // Then
        assertThat(console.getType()).isEqualTo("Console");
        assertThat(pc.getType()).isEqualTo("PC");
        assertThat(vr.getType()).isEqualTo("VR");
    }
}
