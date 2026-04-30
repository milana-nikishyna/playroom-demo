package by.gsu.olaksen.service;

import by.gsu.olaksen.config.AppConfig;
import by.gsu.olaksen.model.Role;
import by.gsu.olaksen.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserService Tests")
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    @DisplayName("Should authenticate user with valid credentials")
    void shouldAuthenticateUserWithValidCredentials() {
        // Given
        String username = "admin";
        String password = "admin123";

        // When
        User user = userService.authenticate(username, password);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo(username);
        assertThat(user.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("Should authenticate regular user with valid credentials")
    void shouldAuthenticateRegularUserWithValidCredentials() {
        // Given
        String username = "user";
        String password = "user123";

        // When
        User user = userService.authenticate(username, password);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo(username);
        assertThat(user.role()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Should return null for invalid password")
    void shouldReturnNullForInvalidPassword() {
        // Given
        String username = "admin";
        String wrongPassword = "wrongpassword";

        // When
        User user = userService.authenticate(username, wrongPassword);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should return null for non-existent user")
    void shouldReturnNullForNonExistentUser() {
        // Given
        String nonExistentUsername = "nonexistent";
        String password = "anypassword";

        // When
        User user = userService.authenticate(nonExistentUsername, password);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should return null for empty username")
    void shouldReturnNullForEmptyUsername() {
        // Given
        String emptyUsername = "";
        String password = "admin123";

        // When
        User user = userService.authenticate(emptyUsername, password);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should return null for empty password")
    void shouldReturnNullForEmptyPassword() {
        // Given
        String username = "admin";
        String emptyPassword = "";

        // When
        User user = userService.authenticate(username, emptyPassword);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should return null for null username")
    void shouldReturnNullForNullUsername() {
        // Given
        String nullUsername = null;
        String password = "admin123";

        // When
        User user = userService.authenticate(nullUsername, password);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should return null for null password")
    void shouldReturnNullForNullPassword() {
        // Given
        String username = "admin";
        String nullPassword = null;

        // When
        User user = userService.authenticate(username, nullPassword);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should be case sensitive for username")
    void shouldBeCaseSensitiveForUsername() {
        // Given
        String username = "ADMIN"; // uppercase
        String password = "admin123";

        // When
        User user = userService.authenticate(username, password);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should be case sensitive for password")
    void shouldBeCaseSensitiveForPassword() {
        // Given
        String username = "admin";
        String password = "ADMIN123"; // uppercase

        // When
        User user = userService.authenticate(username, password);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should handle whitespace in credentials")
    void shouldHandleWhitespaceInCredentials() {
        // Given
        String usernameWithSpace = " admin";
        String passwordWithSpace = "admin123 ";

        // When
        User user = userService.authenticate(usernameWithSpace, passwordWithSpace);

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should load users from AppConfig")
    void shouldLoadUsersFromAppConfig() {
        // Given
        AppConfig config = AppConfig.getInstance();

        // Then
        assertThat(config.getUsers()).isNotEmpty();
        assertThat(config.getUsers()).hasSizeGreaterThanOrEqualTo(2);
    }
}
