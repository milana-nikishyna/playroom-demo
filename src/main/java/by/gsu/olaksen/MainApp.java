package by.gsu.olaksen;

import by.gsu.olaksen.util.FXMLResourceLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        var scene = FXMLResourceLoader.loadScene("login.fxml");

        primaryStage.setTitle("Вход");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}