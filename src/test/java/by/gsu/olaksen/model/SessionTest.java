package by.gsu.olaksen.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Session Singleton Tests")
class SessionTest {

    @BeforeEach
    void setUp() {
        Session.getInstance().clear();
    }

    @AfterEach
    void tearDown() {
        Session.getInstance().clear();
    }

    @Test
    @DisplayName("Should return same instance")
    void shouldReturnSameInstance() {
        // When
        Session instance1 = Session.getInstance();
        Session instance2 = Session.getInstance();

        // Then
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("Should set and get user")
    void shouldSetAndGetUser() {
        // Given
        User user = new User("testuser", "testpass", Role.ADMIN);
        Session session = Session.getInstance();

        // When
        session.setUser(user);

        // Then
        assertThat(session.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should return null when no user is set")
    void shouldReturnNullWhenNoUserIsSet() {
        // Given
        Session session = Session.getInstance();

        // When
        User user = session.getUser();

        // Then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Should clear user")
    void shouldClearUser() {
        // Given
        User user = new User("testuser", "testpass", Role.ADMIN);
        Session session = Session.getInstance();
        session.setUser(user);

        // When
        session.clear();

        // Then
        assertThat(session.getUser()).isNull();
    }

    @Test
    @DisplayName("Should replace existing user")
    void shouldReplaceExistingUser() {
        // Given
        User user1 = new User("user1", "pass1", Role.ADMIN);
        User user2 = new User("user2", "pass2", Role.USER);
        Session session = Session.getInstance();

        // When
        session.setUser(user1);
        assertThat(session.getUser()).isEqualTo(user1);

        session.setUser(user2);

        // Then
        assertThat(session.getUser()).isEqualTo(user2);
        assertThat(session.getUser()).isNotEqualTo(user1);
    }

    @Test
    @DisplayName("Should maintain user across multiple getInstance calls")
    void shouldMaintainUserAcrossMultipleGetInstanceCalls() {
        // Given
        User user = new User("persistent", "pass", Role.ADMIN);

        // When
        Session.getInstance().setUser(user);
        User retrievedUser = Session.getInstance().getUser();

        // Then
        assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Should allow setting user to null")
    void shouldAllowSettingUserToNull() {
        // Given
        User user = new User("testuser", "testpass", Role.ADMIN);
        Session session = Session.getInstance();
        session.setUser(user);

        // When
        session.setUser(null);

        // Then
        assertThat(session.getUser()).isNull();
    }
}
