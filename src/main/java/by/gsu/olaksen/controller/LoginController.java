package by.gsu.olaksen.controller;

import by.gsu.olaksen.service.UserService;
import by.gsu.olaksen.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userService.authenticate(username, password);

        if (user != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../main.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setUser(user);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Учёт");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Неверный логин или пароль");
        }
    }
}