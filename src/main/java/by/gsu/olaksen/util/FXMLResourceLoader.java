package by.gsu.olaksen.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Утилитный класс для загрузки FXML-ресурсов с единообразной обработкой ошибок и разрешением путей.
 * Устраняет дублирование и обеспечивает типобезопасную загрузку ресурсов.
 */
@UtilityClass
public final class FXMLResourceLoader {

    private final String FXML_BASE_PATH = "/by/gsu/olaksen/";
    private final String STYLES_CSS = "styles.css";

    /**
     * Загружает FXML-файл и возвращает настроенный FXMLLoader.
     *
     * @param fxmlFileName Имя FXML-файла (например, "login.fxml")
     * @return Настроенный FXMLLoader
     * @throws IllegalStateException если ресурс не найден
     */
    public FXMLLoader loadFXML(String fxmlFileName) {
        var url = getResource(fxmlFileName);
        return new FXMLLoader(url);
    }

    /**
     * Загружает FXML-файл и возвращает корневой узел Parent.
     *
     * @param fxmlFileName Имя FXML-файла
     * @return Загруженный узел Parent
     * @throws RuntimeException если загрузка не удалась
     */
    public Parent loadView(String fxmlFileName) {
        try {
            var loader = loadFXML(fxmlFileName);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить FXML-представление: " + fxmlFileName, e);
        }
    }

    /**
     * Загружает FXML-файл и возвращает как Parent, так и Controller.
     *
     * @param fxmlFileName Имя FXML-файла
     * @param <T>          Тип контроллера
     * @return ViewWithController, содержащий Parent и Controller
     * @throws RuntimeException если загрузка не удалась
     */
    public <T> ViewWithController<T> loadViewWithController(String fxmlFileName) {
        try {
            var loader = loadFXML(fxmlFileName);
            Parent root = loader.load();
            T controller = loader.getController();
            return new ViewWithController<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить FXML-представление с контроллером: " + fxmlFileName, e);
        }
    }

    /**
     * Загружает FXML и создает Scene с примененной таблицей стилей по умолчанию.
     *
     * @param fxmlFileName Имя FXML-файла
     * @return Scene с загруженным представлением и таблицей стилей
     * @throws RuntimeException если загрузка не удалась
     */
    public Scene loadScene(String fxmlFileName) {
        var root = loadView(fxmlFileName);
        var scene = new Scene(root);
        applyStylesheet(scene);
        return scene;
    }

    /**
     * Загружает FXML, создает Scene и возвращает вместе с контроллером.
     *
     * @param fxmlFileName Имя FXML-файла
     * @param <T>          Тип контроллера
     * @return SceneWithController, содержащий Scene и Controller
     * @throws RuntimeException если загрузка не удалась
     */
    public <T> SceneWithController<T> loadSceneWithController(String fxmlFileName) {
        var viewWithController = FXMLResourceLoader.<T>loadViewWithController(fxmlFileName);
        var scene = new Scene(viewWithController.view());
        applyStylesheet(scene);
        return new SceneWithController<>(scene, viewWithController.controller());
    }

    /**
     * Применяет таблицу стилей по умолчанию к сцене.
     *
     * @param scene Сцена для стилизации
     */
    public void applyStylesheet(Scene scene) {
        var cssUrl = getStylesheet();
        scene.getStylesheets().add(cssUrl.toExternalForm());
    }

    /**
     * Получает URL для файла ресурса.
     *
     * @param fileName Имя файла ресурса
     * @return URL ресурса
     * @throws IllegalStateException если ресурс не найден
     */
    public URL getResource(String fileName) {
        var url = FXMLResourceLoader.class.getResource(FXML_BASE_PATH + fileName);
        return Objects.requireNonNull(url, "Отсутствует ресурс: " + FXML_BASE_PATH + fileName);
    }

    /**
     * Получает URL для таблицы стилей по умолчанию.
     *
     * @return URL файла styles.css
     * @throws IllegalStateException если таблица стилей не найдена
     */
    public URL getStylesheet() {
        return getResource(STYLES_CSS);
    }

    /**
     * Контейнер для представления Parent и его контроллера.
     *
     * @param <T> Тип контроллера
     */
    public record ViewWithController<T>(Parent view, T controller) {
    }

    /**
     * Контейнер для Scene и его контроллера.
     *
     * @param <T> Тип контроллера
     */
    public record SceneWithController<T>(Scene scene, T controller) {
    }
}

