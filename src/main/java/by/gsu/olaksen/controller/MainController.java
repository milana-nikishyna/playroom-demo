package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MainController {

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
            Stage stage = (Stage) roleLabel.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../login.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Вход");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExit() {
        // Закрыть приложение
        javafx.application.Platform.exit();
    }
}