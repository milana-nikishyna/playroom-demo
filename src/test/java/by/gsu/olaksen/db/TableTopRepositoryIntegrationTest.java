package by.gsu.olaksen.db;

import by.gsu.olaksen.model.TableTop;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TableTopRepository Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TableTopRepositoryIntegrationTest {

    private static TableTopRepository repository;

    @BeforeAll
    static void setUpRepository() {
        repository = new TableTopRepository();
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
        List<TableTop> all = repository.getAllTableTops();
        for (TableTop tableTop : all) {
            repository.deleteTableTop(tableTop.getTabletopId());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should add tabletop and return generated id")
    void shouldAddTabletopAndReturnGeneratedId() {
        // Given
        TableTop tableTop = new TableTop(1001, "Монополия");

        // When
        int id = repository.addTableTop(tableTop);

        // Then
        assertThat(id).isPositive();
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve all tabletops")
    void shouldRetrieveAllTabletops() {
        // Given
        TableTop tableTop1 = new TableTop(1001, "Монополия");
        TableTop tableTop2 = new TableTop(1002, "Каркассон");
        TableTop tableTop3 = new TableTop(1003, "Колонизаторы");

        repository.addTableTop(tableTop1);
        repository.addTableTop(tableTop2);
        repository.addTableTop(tableTop3);

        // When
        List<TableTop> allTabletops = repository.getAllTableTops();

        // Then
        assertThat(allTabletops).hasSize(3);
        assertThat(allTabletops)
                .extracting(TableTop::getTabletopName)
                .containsExactlyInAnyOrder("Монополия", "Каркассон", "Колонизаторы");
    }

    @Test
    @Order(3)
    @DisplayName("Should return empty list when no tabletops exist")
    void shouldReturnEmptyListWhenNoTabletopsExist() {
        // When
        List<TableTop> allTabletops = repository.getAllTableTops();

        // Then
        assertThat(allTabletops).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("Should update tabletop")
    void shouldUpdateTabletop() {
        // Given
        TableTop tableTop = new TableTop(1001, "Original Name");
        int id = repository.addTableTop(tableTop);

        TableTop updatedTableTop = new TableTop(id, 2002, "Updated Name");

        // When
        repository.updateTableTop(updatedTableTop);

        // Then
        List<TableTop> allTabletops = repository.getAllTableTops();
        assertThat(allTabletops).hasSize(1);
        TableTop updated = allTabletops.getFirst();
        assertThat(updated.getTabletopName()).isEqualTo("Updated Name");
        assertThat(updated.getTabletopInvNum()).isEqualTo(2002);
    }

    @Test
    @Order(5)
    @DisplayName("Should delete tabletop by id")
    void shouldDeleteTabletopById() {
        // Given
        TableTop tableTop = new TableTop(1001, "To Delete");
        int id = repository.addTableTop(tableTop);

        // When
        repository.deleteTableTop(id);

        // Then
        List<TableTop> allTabletops = repository.getAllTableTops();
        assertThat(allTabletops).isEmpty();
    }

    @Test
    @Order(6)
    @DisplayName("Should preserve inventory number")
    void shouldPreserveInventoryNumber() {
        // Given
        TableTop tableTop = new TableTop(999999, "Test Game");

        // When
        repository.addTableTop(tableTop);

        // Then
        List<TableTop> allTabletops = repository.getAllTableTops();
        assertThat(allTabletops).hasSize(1);
        assertThat(allTabletops.getFirst().getTabletopInvNum()).isEqualTo(999999);
    }

    @Test
    @Order(7)
    @DisplayName("Should handle long names with special characters")
    void shouldHandleLongNamesWithSpecialCharacters() {
        // Given
        String nameWithSpecialChars = "D&D: Подземелья & Драконы (5-е издание) - Базовый набор";
        TableTop tableTop = new TableTop(1001, nameWithSpecialChars);

        // When
        repository.addTableTop(tableTop);

        // Then
        List<TableTop> allTabletops = repository.getAllTableTops();
        assertThat(allTabletops.getFirst().getTabletopName()).isEqualTo(nameWithSpecialChars);
    }

    @Test
    @Order(8)
    @DisplayName("Should allow multiple tabletops with different inventory numbers")
    void shouldAllowMultipleTabletopsWithDifferentInventoryNumbers() {
        // Given
        TableTop tableTop1 = new TableTop(1001, "Game 1");
        TableTop tableTop2 = new TableTop(1002, "Game 2");
        TableTop tableTop3 = new TableTop(1003, "Game 3");

        // When
        repository.addTableTop(tableTop1);
        repository.addTableTop(tableTop2);
        repository.addTableTop(tableTop3);

        // Then
        List<TableTop> allTabletops = repository.getAllTableTops();
        assertThat(allTabletops).hasSize(3);
        assertThat(allTabletops)
                .extracting(TableTop::getTabletopInvNum)
                .containsExactlyInAnyOrder(1001, 1002, 1003);
    }

    @Test
    @Order(9)
    @DisplayName("Should not delete other tabletops when deleting one")
    void shouldNotDeleteOtherTabletopsWhenDeletingOne() {
        // Given
        TableTop tableTop1 = new TableTop(1001, "Keep This");
        TableTop tableTop2 = new TableTop(1002, "Delete This");
        TableTop tableTop3 = new TableTop(1003, "Keep This Too");

        int id2 = repository.addTableTop(tableTop2);

        // When
        repository.addTableTop(tableTop1);
        repository.addTableTop(tableTop3);
        repository.deleteTableTop(id2);

        // Then
        List<TableTop> remaining = repository.getAllTableTops();
        assertThat(remaining).hasSize(2);
        assertThat(remaining)
                .extracting(TableTop::getTabletopName)
                .containsExactlyInAnyOrder("Keep This", "Keep This Too");
    }

    @Test
    @Order(10)
    @DisplayName("Should maintain data integrity after multiple operations")
    void shouldMaintainDataIntegrityAfterMultipleOperations() {
        // Given & When
        TableTop tt1 = new TableTop(1001, "Game 1");
        int id1 = repository.addTableTop(tt1);

        TableTop tt2 = new TableTop(1002, "Game 2");
        int id2 = repository.addTableTop(tt2);

        TableTop updated = new TableTop(id1, 2001, "Game 1 Updated");
        repository.updateTableTop(updated);

        repository.deleteTableTop(id2);

        TableTop tt3 = new TableTop(1003, "Game 3");
        repository.addTableTop(tt3);

        // Then
        List<TableTop> finalList = repository.getAllTableTops();
        assertThat(finalList).hasSize(2);
        assertThat(finalList)
                .extracting(TableTop::getTabletopName)
                .containsExactlyInAnyOrder("Game 1 Updated", "Game 3");
    }
}
