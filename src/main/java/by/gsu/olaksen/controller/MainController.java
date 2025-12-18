package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

    public static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private Label roleLabel;
    @FXML
    private MenuItem currentUserMenuItem;

    public void setUser(User user) {
        roleLabel.setText("Ваша роль: " + user.role());
        if (currentUserMenuItem != null) {
            currentUserMenuItem.setText("Текущий пользователь: " + user.username());
        }
    }

    @FXML
    private void onLogout() {
        try {
            Stage stage = (Stage) roleLabel.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../login.fxml"));
            var scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Вход");
        } catch (Exception e) {
            logger.error("onLogout error", e);
        }
    }

    @FXML
    private void onExit() {
        // Закрыть приложение
        javafx.application.Platform.exit();
    }
}