package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.Session;
import by.gsu.olaksen.model.RentItem;
import by.gsu.olaksen.model.Role;
import by.gsu.olaksen.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;

public class EquipmentTableController {
    @FXML
    private TableView<RentItem> equipmentTable;
    @FXML
    private TableColumn<RentItem, String> modelColumn;
    @FXML
    private TableColumn<RentItem, String> statusColumn;
    @FXML
    private TableColumn<RentItem, String> termColumn;
    @FXML
    private TextField addModelField;
    @FXML
    private Button addButton;

    private final ObservableList<String> statuses = FXCollections.observableArrayList("Свободно", "В аренде");
    private ObservableList<RentItem> items = FXCollections.observableArrayList();
    private boolean isAdmin = false;

    @FXML
    public void initialize() {
        setUser(Session.getInstance().getUser());
        modelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModel()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        termColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerm()));

        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statuses));
        statusColumn.setOnEditCommit(event -> {
            event.getRowValue().setStatus(event.getNewValue());
            equipmentTable.refresh();
        });

        equipmentTable.setItems(items);

        equipmentTable.setRowFactory(_ -> {
            TableRow<RentItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (isAdmin && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && row.isEmpty()) {
                    addNewItem();
                }
            });
            return row;
        });

        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setOnEditCommit(event -> event.getRowValue().setModel(event.getNewValue()));

        termColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        termColumn.setOnEditCommit(event -> event.getRowValue().setTerm(event.getNewValue()));

    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        equipmentTable.setEditable(isAdmin);
        statusColumn.setEditable(isAdmin);
        modelColumn.setEditable(isAdmin);
        termColumn.setEditable(isAdmin);
        addButton.setDisable(!isAdmin);
        addModelField.setDisable(!isAdmin);
    }

    public void setUser(User user) {
        setAdmin(Role.ADMIN == user.role());
    }

    public void setItems(ObservableList<RentItem> items) {
        this.items = items;
        equipmentTable.setItems(items);
    }

    @FXML
    private void onAdd() {
        if (isAdmin && addModelField.getText() != null && !addModelField.getText().isBlank()) {
            items.add(new RentItem(addModelField.getText(), "Свободно", ""));
            addModelField.clear();
        }
    }

    private void addNewItem() {
        if (isAdmin) {
            RentItem newItem = new RentItem("Новая модель", "Свободно", "");
            items.add(newItem);
            equipmentTable.getSelectionModel().select(newItem);
            equipmentTable.scrollTo(newItem);
        }
    }
}