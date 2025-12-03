package by.gsu.olaksen.controller;

import java.net.URL;
import java.util.ResourceBundle;

import by.gsu.olaksen.db.TableTopRepository;
import by.gsu.olaksen.model.TableTop;
import by.gsu.olaksen.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class MainController implements Initializable {

    @FXML private TableView<TableTop> tabletops;
    @FXML private TableColumn<TableTop, Long> tabletopId;
    @FXML private TableColumn<TableTop, String> tabletopName;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Label roleLabel;
    @FXML private MenuItem currentUserMenuItem;
    @FXML private MenuItem logoutMenuItem;

    private final TableTopRepository repository = new TableTopRepository();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabletopId.setCellValueFactory(new PropertyValueFactory<>("tabletopId"));
        tabletopName.setCellValueFactory(new PropertyValueFactory<>("tabletopName"));

        // Редактирование столбца "Наименование"
        tabletopName.setCellFactory(TextFieldTableCell.forTableColumn());
        tabletopName.setOnEditCommit(event -> {
            TableTop oldTableTop = event.getRowValue();
            String newName = event.getNewValue();
            TableTop newTableTop = new TableTop(oldTableTop.getTabletopId(), newName);

            // Обновить в БД
            repository.updateTableTop(newTableTop);

            // Заменить в ObservableList
            int index = tabletops.getItems().indexOf(oldTableTop);
            if (index >= 0) {
                tabletops.getItems().set(index, newTableTop);
            }
        });

        // Загружаем данные из БД
        tabletops.getItems().setAll(repository.getAllTableTops());
    }

    public void setUser(User user) {
        roleLabel.setText("Ваша роль: " + user.getRole());
        boolean isAdmin = "admin".equals(user.getRole());

        addButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
        idField.setDisable(!isAdmin);
        nameField.setDisable(!isAdmin);
        tabletops.setEditable(isAdmin);
        tabletopName.setEditable(isAdmin);

        if (currentUserMenuItem != null) {
            currentUserMenuItem.setText("Текущий пользователь: " + user.getUsername());
        }
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
            // обработка ошибки
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

    @FXML
    private void onLogout() {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) tabletops.getScene().getWindow();
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("../login.fxml"));
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Вход");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}