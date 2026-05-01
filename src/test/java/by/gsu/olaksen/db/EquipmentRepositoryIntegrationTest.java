package by.gsu.olaksen.db;

import by.gsu.olaksen.model.Equipment;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EquipmentRepository Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipmentRepositoryIntegrationTest {

    private static EquipmentRepository repository;
    private static final LocalDateTime FIXED_RENT_UNTIL = LocalDateTime.of(2026, 5, 2, 12, 0);

    @BeforeAll
    static void setUpRepository() {
        repository = new EquipmentRepository();
    }

    @BeforeEach
    void setUp() {
        clearDatabase();
    }

    @AfterEach
    void tearDown() {
        clearDatabase();
    }

    private void clearDatabase() {
        List<Equipment> all = repository.getAll();
        for (Equipment equipment : all) {
            repository.delete(equipment.getId());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should add equipment and return generated id")
    void shouldAddEquipmentAndReturnGeneratedId() {
        // Given
        Equipment equipment = new Equipment("Xbox Series X", "Свободно", null, "Console");
        equipment.setPricePerHour(new BigDecimal("50.0"));

        // When
        int id = repository.add(equipment);

        // Then
        assertThat(id).isPositive();
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve all equipment")
    void shouldRetrieveAllEquipment() {
        // Given
        Equipment equipment1 = new Equipment("PlayStation 5", "Свободно", null, "Console");
        equipment1.setPricePerHour(new BigDecimal("45.0"));
        Equipment equipment2 = new Equipment("Gaming PC", "В аренде", FIXED_RENT_UNTIL, "PC");
        equipment2.setPricePerHour(new BigDecimal("75.0"));

        repository.add(equipment1);
        repository.add(equipment2);

        // When
        List<Equipment> allEquipment = repository.getAll();

        // Then
        assertThat(allEquipment).hasSize(2);
        assertThat(allEquipment)
                .extracting(Equipment::getModel)
                .containsExactlyInAnyOrder("PlayStation 5", "Gaming PC");
    }

    @Test
    @Order(3)
    @DisplayName("Should return empty list when no equipment exists")
    void shouldReturnEmptyListWhenNoEquipmentExists() {
        // When
        List<Equipment> allEquipment = repository.getAll();

        // Then
        assertThat(allEquipment).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("Should get equipment by type")
    void shouldGetEquipmentByType() {
        // Given
        Equipment console1 = new Equipment("Xbox Series X", "Свободно", null, "Console");
        Equipment console2 = new Equipment("PlayStation 5", "Свободно", null, "Console");
        Equipment pc = new Equipment("Gaming PC", "Свободно", null, "PC");

        repository.add(console1);
        repository.add(console2);
        repository.add(pc);

        // When
        List<Equipment> consoles = repository.getByType("Console");

        // Then
        assertThat(consoles).hasSize(2);
        assertThat(consoles)
                .extracting(Equipment::getType)
                .containsOnly("Console");
    }

    @Test
    @Order(5)
    @DisplayName("Should return empty list for non-existent type")
    void shouldReturnEmptyListForNonExistentType() {
        // Given
        Equipment equipment = new Equipment("Test", "Свободно", null, "Console");
        repository.add(equipment);

        // When
        List<Equipment> result = repository.getByType("NonExistent");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @Order(6)
    @DisplayName("Should update equipment")
    void shouldUpdateEquipment() {
        // Given
        Equipment equipment = new Equipment("Original Model", "Свободно", null, "Console");
        equipment.setPricePerHour(new BigDecimal("50.0"));
        int id = repository.add(equipment);

        equipment.setId(id);
        equipment.setModel("Updated Model");
        equipment.setStatus("В аренде");
        equipment.setRentUntil(FIXED_RENT_UNTIL);
        equipment.setPricePerHour(new BigDecimal("60.0"));

        // When
        repository.update(equipment);

        // Then
        List<Equipment> allEquipment = repository.getAll();
        assertThat(allEquipment).hasSize(1);
        Equipment updated = allEquipment.getFirst();
        assertThat(updated.getModel()).isEqualTo("Updated Model");
        assertThat(updated.getStatus()).isEqualTo("В аренде");
        assertThat(updated.getRentUntil()).isEqualTo(FIXED_RENT_UNTIL);
        assertThat(updated.getPricePerHour()).isEqualTo(new BigDecimal("60.0"));
    }

    @Test
    @Order(7)
    @DisplayName("Should delete equipment by id")
    void shouldDeleteEquipmentById() {
        // Given
        Equipment equipment = new Equipment("To Delete", "Свободно", null, "Console");
        int id = repository.add(equipment);

        // When
        repository.delete(id);

        // Then
        List<Equipment> allEquipment = repository.getAll();
        assertThat(allEquipment).isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("Should handle status conversion correctly")
    void shouldHandleStatusConversionCorrectly() {
        // Given
        Equipment available = new Equipment("Available", "Свободно", null, "Console");
        Equipment rented = new Equipment("Rented", "В аренде", FIXED_RENT_UNTIL, "Console");

        repository.add(available);
        repository.add(rented);

        // When
        List<Equipment> allEquipment = repository.getAll();

        // Then
        assertThat(allEquipment).hasSize(2);
        assertThat(allEquipment)
                .extracting(Equipment::getStatus)
                .containsExactlyInAnyOrder("Свободно", "В аренде");
    }

    @Test
    @Order(9)
    @DisplayName("Should preserve price per hour with decimal precision")
    void shouldPreservePricePerHourWithDecimalPrecision() {
        // Given
        Equipment equipment = new Equipment("Test", "Свободно", null, "Console");
        equipment.setPricePerHour(new BigDecimal("123.45"));

        // When
        repository.add(equipment);

        // Then
        List<Equipment> allEquipment = repository.getAll();
        assertThat(allEquipment).hasSize(1);
        assertThat(allEquipment.getFirst().getPricePerHour()).isEqualTo(new BigDecimal("123.45"));
    }

    @Test
    @Order(10)
    @DisplayName("Should handle concurrent additions")
    void shouldHandleConcurrentAdditions() {
        // Given
        Equipment equipment1 = new Equipment("Equipment 1", "Свободно", null, "Console");
        Equipment equipment2 = new Equipment("Equipment 2", "Свободно", null, "PC");
        Equipment equipment3 = new Equipment("Equipment 3", "Свободно", null, "VR");

        // When
        int id1 = repository.add(equipment1);
        int id2 = repository.add(equipment2);
        int id3 = repository.add(equipment3);

        // Then
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id2).isNotEqualTo(id3);
        assertThat(id1).isNotEqualTo(id3);

        List<Equipment> allEquipment = repository.getAll();
        assertThat(allEquipment).hasSize(3);
    }
}
