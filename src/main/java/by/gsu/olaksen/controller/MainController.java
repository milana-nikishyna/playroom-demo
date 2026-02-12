package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.User;
import by.gsu.olaksen.util.FXMLResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

    public static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private Label roleLabel;
    @FXML private MenuItem currentUserMenuItem;

    public void setUser(User user) {
        roleLabel.setText("Ваша роль: " + user.role());
        if (currentUserMenuItem != null) {
            currentUserMenuItem.setText("Текущий пользователь: " + user.username());
        }
    }

    @FXML
    private void onLogout() {
        try {
            var stage = (Stage) roleLabel.getScene().getWindow();
            var scene = FXMLResourceLoader.loadScene("login.fxml");
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