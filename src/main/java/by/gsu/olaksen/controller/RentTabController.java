package by.gsu.olaksen.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public class RentTabController {

    @FXML private Tab consoleTab;
    @FXML private Tab gameTab;
    @FXML private Tab gamepadTab;

    // type values used in DB (you can change these to match your schema)
    private static final String TYPE_CONSOLE = "console";
    private static final String TYPE_GAME = "game";
    private static final String TYPE_GAMEPAD = "gamepad";

    @FXML
    public void initialize() {
        try {
            // consoles tab
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../equipment_table.fxml"));
            Parent consoleContent = loader1.load();
            EquipmentTableController consoleTableController = loader1.getController();
            consoleTableController.loadEquipmentByType(TYPE_CONSOLE);
            consoleTab.setContent(consoleContent);

            // games tab
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("../equipment_table.fxml"));
            Parent gameContent = loader2.load();
            EquipmentTableController gameTableController = loader2.getController();
            gameTableController.loadEquipmentByType(TYPE_GAME);
            gameTab.setContent(gameContent);

            // gamepads tab
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("../equipment_table.fxml"));
            Parent gamepadContent = loader3.load();
            EquipmentTableController gamepadTableController = loader3.getController();
            gamepadTableController.loadEquipmentByType(TYPE_GAMEPAD);
            gamepadTab.setContent(gamepadContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load equipment tables", e);
        }
    }
}