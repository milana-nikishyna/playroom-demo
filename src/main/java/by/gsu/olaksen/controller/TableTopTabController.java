package by.gsu.olaksen.controller;

import by.gsu.olaksen.db.TableTopRepository;
import by.gsu.olaksen.model.Role;
import by.gsu.olaksen.model.Session;
import by.gsu.olaksen.model.TableTop;
import by.gsu.olaksen.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableTopTabController {
    private static final Logger logger = LoggerFactory.getLogger(TableTopTabController.class);

    @FXML private TableView<TableTop> tabletops;
    @FXML private TableColumn<TableTop, Integer> tabletopInvNum;
    @FXML private TableColumn<TableTop, String> tabletopName;
    @FXML private TextField invNumField;
    @FXML private TextField nameField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;

    private final TableTopRepository repository = new TableTopRepository();

    @FXML
    public void initialize() {
        tabletopInvNum.setCellValueFactory(new PropertyValueFactory<>("tabletopInvNum"));
        tabletopName.setCellValueFactory(new PropertyValueFactory<>("tabletopName"));

        tabletopInvNum.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        tabletopInvNum.setOnEditCommit(event -> {
            var oldTableTop = event.getRowValue();
            var newInvNum = event.getNewValue();
            if (newInvNum == null) {
                return;
            }
            var updated = new TableTop(oldTableTop.getTabletopId(), newInvNum, oldTableTop.getTabletopName());
            repository.updateTableTop(updated);

            var index = tabletops.getItems().indexOf(oldTableTop);
            if (index >= 0) {
                tabletops.getItems().set(index, updated);
            }
        });

        tabletopName.setCellFactory(TextFieldTableCell.forTableColumn());
        tabletopName.setOnEditCommit(event -> {
            var oldTableTop = event.getRowValue();
            var newName = event.getNewValue();
            var updated = new TableTop(oldTableTop.getTabletopId(), oldTableTop.getTabletopInvNum(), newName);

            repository.updateTableTop(updated);

            var index = tabletops.getItems().indexOf(oldTableTop);
            if (index >= 0) {
                tabletops.getItems().set(index, updated);
            }
        });

        if (invNumField != null) {
            invNumField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
                var text = change.getControlNewText();
                return text.matches("\\d*") ? change : null;
            }));
        }

        tabletops.getItems().setAll(repository.getAllTableTops());
        setUser(Session.getInstance().getUser());
    }

    public void setUser(User user) {
        setAdmin(Role.ADMIN == user.role());
    }

    private void setAdmin(boolean isAdmin) {
        // скрываем поля добавления/удаления для user
        if (addButton != null) {
            addButton.setVisible(isAdmin);
        }
        if (deleteButton != null) {
            deleteButton.setVisible(isAdmin);
        }
        if (nameField != null) {
            nameField.setVisible(isAdmin);
        }
        if (invNumField != null) {
            invNumField.setVisible(isAdmin);
        }
        tabletops.setEditable(isAdmin);
        tabletopName.setEditable(isAdmin);
        tabletopInvNum.setEditable(isAdmin);
    }

    @FXML
    private void onAdd() {
        try {
            var name = nameField.getText();
            var invNumText = invNumField.getText();
            if (name == null || name.isBlank() || invNumText == null || invNumText.isBlank()) {
                return;
            }
            var invNum = Integer.parseInt(invNumText);
            var tabletop = new TableTop(invNum, name);
            var id = repository.addTableTop(tabletop);
            tabletop.setTabletopId(id);
            tabletops.getItems().add(tabletop);
            nameField.clear();
            invNumField.clear();
        } catch (NumberFormatException e) {
            logger.error("Invalid inventory number", e);
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