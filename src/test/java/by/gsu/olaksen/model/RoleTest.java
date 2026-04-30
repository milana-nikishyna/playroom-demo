package by.gsu.olaksen.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Role Enum Tests")
class RoleTest {

    @Test
    @DisplayName("Should have ADMIN role")
    void shouldHaveAdminRole() {
        // When
        Role role = Role.ADMIN;

        // Then
        assertThat(role).isNotNull();
        assertThat(role.name()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Should have USER role")
    void shouldHaveUserRole() {
        // When
        Role role = Role.USER;

        // Then
        assertThat(role).isNotNull();
        assertThat(role.name()).isEqualTo("USER");
    }

    @Test
    @DisplayName("Should convert string to Role enum")
    void shouldConvertStringToRoleEnum() {
        // When
        Role adminRole = Role.valueOf("ADMIN");
        Role userRole = Role.valueOf("USER");

        // Then
        assertThat(adminRole).isEqualTo(Role.ADMIN);
        assertThat(userRole).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Should have exactly 2 roles")
    void shouldHaveExactlyTwoRoles() {
        // When
        Role[] roles = Role.values();

        // Then
        assertThat(roles).hasSize(2);
        assertThat(roles).contains(Role.ADMIN, Role.USER);
    }
}
