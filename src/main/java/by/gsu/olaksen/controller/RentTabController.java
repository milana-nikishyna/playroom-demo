package by.gsu.olaksen.controller;

import by.gsu.olaksen.util.FXMLResourceLoader;
import javafx.fxml.FXML;
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
            loadEquipmentTab(consoleTab, TYPE_CONSOLE);
            loadEquipmentTab(gameTab, TYPE_GAME);
            loadEquipmentTab(gamepadTab, TYPE_GAMEPAD);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load equipment tables", e);
        }
    }

    private void loadEquipmentTab(Tab tab, String equipmentType) {
        var viewWithController = FXMLResourceLoader.<EquipmentTableController>loadViewWithController("equipment_table.fxml");
        viewWithController.controller().loadEquipmentByType(equipmentType);
        tab.setContent(viewWithController.view());
    }
}