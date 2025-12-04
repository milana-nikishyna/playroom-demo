package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.Session;
import by.gsu.olaksen.db.TableTopRepository;
import by.gsu.olaksen.model.Role;
import by.gsu.olaksen.model.TableTop;
import by.gsu.olaksen.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class TableTopTabController {

    @FXML
    private TableView<TableTop> tabletops;
    @FXML
    private TableColumn<TableTop, Long> tabletopId;
    @FXML
    private TableColumn<TableTop, String> tabletopName;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;

    private final TableTopRepository repository = new TableTopRepository();

    @FXML
    public void initialize() {
        tabletopId.setCellValueFactory(new PropertyValueFactory<>("tabletopId"));
        tabletopName.setCellValueFactory(new PropertyValueFactory<>("tabletopName"));

        tabletopName.setCellFactory(TextFieldTableCell.forTableColumn());
        tabletopName.setOnEditCommit(event -> {
            TableTop oldTableTop = event.getRowValue();
            String newName = event.getNewValue();
            TableTop newTableTop = new TableTop(oldTableTop.getTabletopId(), newName);

            repository.updateTableTop(newTableTop);

            int index = tabletops.getItems().indexOf(oldTableTop);
            if (index >= 0) {
                tabletops.getItems().set(index, newTableTop);
            }
        });

        tabletops.getItems().setAll(repository.getAllTableTops());
        setUser(Session.getInstance().getUser());
    }

    public void setUser(User user) {
        setAdmin(Role.ADMIN == user.role());
    }

    private void setAdmin(boolean isAdmin) {
        addButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
        idField.setDisable(!isAdmin);
        nameField.setDisable(!isAdmin);
        tabletops.setEditable(isAdmin);
        tabletopName.setEditable(isAdmin);
    }

    @FXML
    private void onAdd() {
        try {
            Long id = Long.parseLong(idField.getText());
            String name = nameField.getText();
            TableTop tabletop = new TableTop(id, name);
            repository.addTableTop(tabletop);
            tabletops.getItems().add(tabletop);
            idField.clear();
            nameField.clear();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDelete() {
        TableTop selected = tabletops.getSelectionModel().getSelectedItem();
        if (selected != null) {
            repository.deleteTableTop(selected.getTabletopId());
            tabletops.getItems().remove(selected);
        }
    }
}