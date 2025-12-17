package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.Session;
import by.gsu.olaksen.db.EquipmentRepository;
import by.gsu.olaksen.model.Equipment;
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
    private TableView<Equipment> equipmentTable;
    @FXML
    private TableColumn<Equipment, String> modelColumn;
    @FXML
    private TableColumn<Equipment, String> statusColumn;
    @FXML
    private TableColumn<Equipment, String> termColumn;
    @FXML
    private TextField addModelField;
    @FXML
    private Button addButton;

    private final ObservableList<String> statuses = FXCollections.observableArrayList("Свободно", "В аренде");
    private ObservableList<Equipment> items = FXCollections.observableArrayList();
    private boolean isAdmin = false;
    private final EquipmentRepository repository = new EquipmentRepository();

    @FXML
    public void initialize() {
        //TODO: update table ui
        setUser(Session.getInstance().getUser());
        modelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModel()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        termColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerm()));


        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statuses));
        statusColumn.setOnEditCommit(event -> {
            event.getRowValue().setStatus(event.getNewValue());
            equipmentTable.refresh();
        });

        equipmentTable.setItems(repository.getAll());

        equipmentTable.setRowFactory(_ -> {
            TableRow<Equipment> row = new TableRow<>();
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

    public void setItems(ObservableList<Equipment> items) {
        this.items = items;
        equipmentTable.setItems(items);
    }

    @FXML
    private void onAdd() {
        if (isAdmin && addModelField.getText() != null && !addModelField.getText().isBlank()) {
            repository.add(new Equipment(addModelField.getText(), "Свободно", ""));
            items.add(new Equipment(addModelField.getText(), "Свободно", ""));
            addModelField.clear();
        }
    }

    private void addNewItem() {
        if (isAdmin) {
            Equipment newItem = new Equipment("Новая модель", "Свободно", "");
            items.add(newItem);
            equipmentTable.getSelectionModel().select(newItem);
            equipmentTable.scrollTo(newItem);
        }
    }
}