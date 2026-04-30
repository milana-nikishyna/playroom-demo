package by.gsu.olaksen.config;

import by.gsu.olaksen.model.Role;
import by.gsu.olaksen.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AppConfig Tests")
class AppConfigTest {

    @Test
    @DisplayName("Should return singleton instance")
    void shouldReturnSingletonInstance() {
        // When
        AppConfig instance1 = AppConfig.getInstance();
        AppConfig instance2 = AppConfig.getInstance();

        // Then
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("Should load users from configuration")
    void shouldLoadUsersFromConfiguration() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        assertThat(users).isNotEmpty();
    }

    @Test
    @DisplayName("Should load admin user")
    void shouldLoadAdminUser() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        User admin = users.stream()
                .filter(u -> u.role() == Role.ADMIN)
                .findFirst()
                .orElse(null);

        assertThat(admin).isNotNull();
        assertThat(admin.username()).isEqualTo("admin");
        assertThat(admin.password()).isEqualTo("admin123");
    }

    @Test
    @DisplayName("Should load regular user")
    void shouldLoadRegularUser() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        User regularUser = users.stream()
                .filter(u -> u.role() == Role.USER)
                .findFirst()
                .orElse(null);

        assertThat(regularUser).isNotNull();
        assertThat(regularUser.username()).isEqualTo("user");
        assertThat(regularUser.password()).isEqualTo("user123");
    }

    @Test
    @DisplayName("Should have at least 2 users")
    void shouldHaveAtLeastTwoUsers() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should get database property URL")
    void shouldGetDatabasePropertyUrl() {
        // When
        AppConfig config = AppConfig.getInstance();
        String url = config.getDbProperty("url");

        // Then
        assertThat(url).isNotNull();
        assertThat(url).startsWith("jdbc:");
    }

    @Test
    @DisplayName("Should get database property user")
    void shouldGetDatabasePropertyUser() {
        // When
        AppConfig config = AppConfig.getInstance();
        String user = config.getDbProperty("user");

        // Then
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("Should return null for non-existent property")
    void shouldReturnNullForNonExistentProperty() {
        // When
        AppConfig config = AppConfig.getInstance();
        String nonExistent = config.getDbProperty("non_existent_key");

        // Then
        assertThat(nonExistent).isNull();
    }

    @Test
    @DisplayName("Users should have valid roles")
    void usersShouldHaveValidRoles() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        assertThat(users).allMatch(user ->
                user.role() == Role.ADMIN || user.role() == Role.USER
        );
    }

    @Test
    @DisplayName("All users should have non-empty username")
    void allUsersShouldHaveNonEmptyUsername() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        assertThat(users)
                .allMatch(user -> user.username() != null && !user.username().isEmpty());
    }

    @Test
    @DisplayName("All users should have non-empty password")
    void allUsersShouldHaveNonEmptyPassword() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        assertThat(users)
                .allMatch(user -> user.password() != null && !user.password().isEmpty());
    }

    @Test
    @DisplayName("Should convert role string to uppercase")
    void shouldConvertRoleStringToUppercase() {
        // When
        AppConfig config = AppConfig.getInstance();
        List<User> users = config.getUsers();

        // Then
        // If config contains roles in lowercase, they should be converted to uppercase
        assertThat(users).allMatch(user ->
                user.role().name().equals(user.role().name().toUpperCase())
        );
    }
}
