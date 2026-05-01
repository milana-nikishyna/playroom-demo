package by.gsu.olaksen;

import by.gsu.olaksen.util.FXMLResourceLoader;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Регистрируем шрифт Inter для всего приложения
        Font.loadFont(getClass().getResourceAsStream("/by/gsu/olaksen/fonts/Inter-Regular.ttf"), 13);
        Font.loadFont(getClass().getResourceAsStream("/by/gsu/olaksen/fonts/Inter-Bold.ttf"), 13);

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