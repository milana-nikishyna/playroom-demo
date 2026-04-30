package by.gsu.olaksen.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Model Tests")
class UserTest {

    @Test
    @DisplayName("Should create User with valid data")
    void shouldCreateUserWithValidData() {
        // Given
        String username = "testuser";
        String password = "testpass";
        Role role = Role.ADMIN;

        // When
        User user = new User(username, password, role);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo(username);
        assertThat(user.password()).isEqualTo(password);
        assertThat(user.role()).isEqualTo(role);
    }

    @Test
    @DisplayName("Should create User with USER role")
    void shouldCreateUserWithUserRole() {
        // Given & When
        User user = new User("user", "pass", Role.USER);

        // Then
        assertThat(user.role()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Two users with same data should be equal")
    void twoUsersWithSameDataShouldBeEqual() {
        // Given
        User user1 = new User("john", "pass123", Role.ADMIN);
        User user2 = new User("john", "pass123", Role.ADMIN);

        // Then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Two users with different data should not be equal")
    void twoUsersWithDifferentDataShouldNotBeEqual() {
        // Given
        User user1 = new User("john", "pass123", Role.ADMIN);
        User user2 = new User("jane", "pass456", Role.USER);

        // Then
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("User should have proper toString representation")
    void userShouldHaveProperToString() {
        // Given
        User user = new User("admin", "secret", Role.ADMIN);

        // When
        String toString = user.toString();

        // Then
        assertThat(toString).contains("admin");
        assertThat(toString).contains("secret");
        assertThat(toString).contains("ADMIN");
    }
}
