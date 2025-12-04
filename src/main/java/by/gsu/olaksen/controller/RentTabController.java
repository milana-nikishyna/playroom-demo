package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.RentItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public class RentTabController {

    @FXML private Tab consoleTab;
    @FXML private Tab gameTab;
    @FXML private Tab gamepadTab;

    private final ObservableList<RentItem> consoles = FXCollections.observableArrayList(
            new RentItem("PS5", "Свободно", ""),
            new RentItem("Xbox Series X", "В аренде", "до 2025-12-10")
    );
    private final ObservableList<RentItem> games = FXCollections.observableArrayList(
            new RentItem("FIFA 23", "Свободно", ""),
            new RentItem("Cyberpunk 2077", "В аренде", "до 2025-12-08")
    );
    private final ObservableList<RentItem> gamepads = FXCollections.observableArrayList(
            new RentItem("DualSense", "Свободно", ""),
            new RentItem("Xbox Wireless", "В аренде", "до 2025-12-09")
    );

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../equipment_table.fxml"));
            Parent consoleContent = loader1.load();
            EquipmentTableController consoleTableController = loader1.getController();
            consoleTableController.setItems(consoles);
            consoleTab.setContent(consoleContent);



            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("../equipment_table.fxml"));
            Parent gameContent = loader2.load();
            EquipmentTableController gameTableController = loader2.getController();
            gameTableController.setItems(games);
            gameTab.setContent(gameContent);


            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("../equipment_table.fxml"));
            Parent gamepadContent = loader3.load();
            EquipmentTableController gamepadTableController = loader3.getController();
            gamepadTableController.setItems(gamepads);
            gamepadTab.setContent(gamepadContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load equipment tables", e);
        }
    }
}