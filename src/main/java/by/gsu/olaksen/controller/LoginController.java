package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.Session;
import by.gsu.olaksen.service.UserService;
import by.gsu.olaksen.util.FXMLResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    private void onLogin() {
        var username = usernameField.getText();
        var password = passwordField.getText();
        var user = userService.authenticate(username, password);

        if (user != null) {
            Session.getInstance().setUser(user);
            try {
                var sceneWithController = FXMLResourceLoader.<MainController>loadSceneWithController("main.fxml");
                sceneWithController.controller().setUser(user);

                var stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("Учёт");
                stage.setScene(sceneWithController.scene());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Неверный логин или пароль");
        }
    }
}