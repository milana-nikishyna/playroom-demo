package by.gsu.olaksen;

import by.gsu.olaksen.util.FXMLResourceLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        var scene = FXMLResourceLoader.loadScene("login.fxml");

        primaryStage.setTitle("Вход");

        // Устанавливаем Scene ДО установки размеров
        primaryStage.setScene(scene);

        // Для окна логина устанавливаем компактные размеры
        primaryStage.setResizable(false); // Запрещаем изменять размер окна логина
        primaryStage.sizeToScene(); // Автоматически подбираем размер окна под Scene
    
        primaryStage.show();
    }
}