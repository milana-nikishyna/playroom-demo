package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.Session;
import by.gsu.olaksen.model.User;
import by.gsu.olaksen.util.FXMLResourceLoader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainController {

    public static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private Label roleLabel;
    @FXML private MenuItem currentUserMenuItem;
    // Форматтер для даты и времени в формате "ПН 25.12.2025 18:21:11"
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("EE dd.MM.yyyy HH:mm:ss", Locale.of("ru"));
    @FXML private Label dateTimeLabel;
    private Timeline clockTimeline;

    /**
     * Преобразует день недели в верхний регистр для красивого отображения
     */
    private String formatDateTimeUpperCase(LocalDateTime dateTime) {
        String formatted = dateTime.format(DATE_TIME_FORMATTER);
        // Находим первые два символа (день недели) и делаем их заглавными
        if (formatted.length() >= 2) {
            return formatted.substring(0, 2).toUpperCase() + formatted.substring(2);
        }
        return formatted;
    }

    @FXML
    public void initialize() {
        startClock();
    }

    /**
     * Запускает живые часы, которые обновляются каждую секунду
     */
    private void startClock() {
        if (dateTimeLabel != null) {
            // Немедленно обновляем время
            updateDateTime();

            // Создаем Timeline для обновления каждую секунду
            clockTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), _ -> updateDateTime())
            );
            clockTimeline.setCycleCount(Animation.INDEFINITE);
            clockTimeline.play();
        }
    }

    /**
     * Обновляет отображение даты и времени
     */
    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = formatDateTimeUpperCase(now);
        dateTimeLabel.setText(formattedDateTime);
    }

    public void setUser(User user) {
        roleLabel.setText("Ваша роль: " + user.role());
        if (currentUserMenuItem != null) {
            currentUserMenuItem.setText("Текущий пользователь: " + user.username());
        }
    }

    @FXML
    private void onLogout() {
        try {
            // Останавливаем часы при выходе
            if (clockTimeline != null) {
                clockTimeline.stop();
            }

            Session.getInstance().clear();
            var stage = (Stage) roleLabel.getScene().getWindow();
            var scene = FXMLResourceLoader.loadScene("login.fxml");

            // Возвращаем настройки окна логина
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Вход");
            stage.sizeToScene();

            // Центрируем окно логина
            stage.centerOnScreen();
        } catch (Exception e) {
            logger.error("onLogout error", e);
        }
    }

    @FXML
    private void onExit() {
        // Останавливаем часы перед закрытием
        if (clockTimeline != null) {
            clockTimeline.stop();
        }
        // Закрыть приложение
        javafx.application.Platform.exit();
    }
}