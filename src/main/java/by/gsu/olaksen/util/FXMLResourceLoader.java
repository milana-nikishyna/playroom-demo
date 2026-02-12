package by.gsu.olaksen.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Utility class for loading FXML resources with consistent error handling and path resolution.
 * Eliminates duplication and provides type-safe resource loading.
 */
@UtilityClass
public final class FXMLResourceLoader {

    private final String FXML_BASE_PATH = "/by/gsu/olaksen/";
    private final String STYLES_CSS = "styles.css";

    /**
     * Load FXML file and return configured FXMLLoader.
     *
     * @param fxmlFileName The FXML file name (e.g., "login.fxml")
     * @return Configured FXMLLoader
     * @throws IllegalStateException if resource is not found
     */
    public FXMLLoader loadFXML(String fxmlFileName) {
        var url = getResource(fxmlFileName);
        return new FXMLLoader(url);
    }

    /**
     * Load FXML file and return the root Parent node.
     *
     * @param fxmlFileName The FXML file name
     * @return Loaded Parent node
     * @throws RuntimeException if loading fails
     */
    public Parent loadView(String fxmlFileName) {
        try {
            var loader = loadFXML(fxmlFileName);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML view: " + fxmlFileName, e);
        }
    }

    /**
     * Load FXML file and return both Parent and Controller.
     *
     * @param fxmlFileName The FXML file name
     * @param <T>          Controller type
     * @return ViewWithController containing both Parent and Controller
     * @throws RuntimeException if loading fails
     */
    public <T> ViewWithController<T> loadViewWithController(String fxmlFileName) {
        try {
            var loader = loadFXML(fxmlFileName);
            Parent root = loader.load();
            T controller = loader.getController();
            return new ViewWithController<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML view with controller: " + fxmlFileName, e);
        }
    }

    /**
     * Load FXML and create a Scene with default stylesheet applied.
     *
     * @param fxmlFileName The FXML file name
     * @return Scene with loaded view and stylesheet
     * @throws RuntimeException if loading fails
     */
    public Scene loadScene(String fxmlFileName) {
        var root = loadView(fxmlFileName);
        var scene = new Scene(root);
        applyStylesheet(scene);
        return scene;
    }

    /**
     * Load FXML, create Scene, and return with controller.
     *
     * @param fxmlFileName The FXML file name
     * @param <T>          Controller type
     * @return SceneWithController containing Scene and Controller
     * @throws RuntimeException if loading fails
     */
    public <T> SceneWithController<T> loadSceneWithController(String fxmlFileName) {
        var viewWithController = FXMLResourceLoader.<T>loadViewWithController(fxmlFileName);
        var scene = new Scene(viewWithController.view());
        applyStylesheet(scene);
        return new SceneWithController<>(scene, viewWithController.controller());
    }

    /**
     * Apply default stylesheet to a scene.
     *
     * @param scene The scene to style
     */
    public void applyStylesheet(Scene scene) {
        var cssUrl = getStylesheet();
        scene.getStylesheets().add(cssUrl.toExternalForm());
    }

    /**
     * Get URL for a resource file.
     *
     * @param fileName The resource file name
     * @return URL of the resource
     * @throws IllegalStateException if resource is not found
     */
    public URL getResource(String fileName) {
        var url = FXMLResourceLoader.class.getResource(FXML_BASE_PATH + fileName);
        return Objects.requireNonNull(url, "Missing resource: " + FXML_BASE_PATH + fileName);
    }

    /**
     * Get URL for the default stylesheet.
     *
     * @return URL of styles.css
     * @throws IllegalStateException if stylesheet is not found
     */
    public URL getStylesheet() {
        return getResource(STYLES_CSS);
    }

    /**
     * Container for Parent view and its Controller.
     *
     * @param <T> Controller type
     */
    public record ViewWithController<T>(Parent view, T controller) {
    }

    /**
     * Container for Scene and its Controller.
     *
     * @param <T> Controller type
     */
    public record SceneWithController<T>(Scene scene, T controller) {
    }
}

