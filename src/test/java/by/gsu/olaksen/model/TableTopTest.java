package by.gsu.olaksen.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TableTop Model Tests")
class TableTopTest {

    @Test
    @DisplayName("Should create TableTop with all fields")
    void shouldCreateTableTopWithAllFields() {
        // Given
        int id = 1;
        int invNum = 1001;
        String name = "Монополия";

        // When
        TableTop tableTop = new TableTop(id, invNum, name);

        // Then
        assertThat(tableTop).isNotNull();
        assertThat(tableTop.getTabletopId()).isEqualTo(id);
        assertThat(tableTop.getTabletopInvNum()).isEqualTo(invNum);
        assertThat(tableTop.getTabletopName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Should create TableTop with required fields only")
    void shouldCreateTableTopWithRequiredFieldsOnly() {
        // Given
        int invNum = 2001;
        String name = "Каркассон";

        // When
        TableTop tableTop = new TableTop(invNum, name);

        // Then
        assertThat(tableTop).isNotNull();
        assertThat(tableTop.getTabletopInvNum()).isEqualTo(invNum);
        assertThat(tableTop.getTabletopName()).isEqualTo(name);
        assertThat(tableTop.getTabletopId()).isZero();
    }

    @Test
    @DisplayName("Should update TableTop id using setter")
    void shouldUpdateTableTopIdUsingSetter() {
        // Given
        TableTop tableTop = new TableTop(1001, "Игра");

        // When
        tableTop.setTabletopId(5);

        // Then
        assertThat(tableTop.getTabletopId()).isEqualTo(5);
    }
}
