package by.gsu.olaksen.controller;

import by.gsu.olaksen.db.EquipmentRepository;
import by.gsu.olaksen.model.Equipment;
import by.gsu.olaksen.model.Role;
import by.gsu.olaksen.model.Session;
import by.gsu.olaksen.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;

import java.math.BigDecimal;

public class EquipmentTableController {
    @FXML private TableView<Equipment> equipmentTable;
    @FXML private TableColumn<Equipment, String> modelColumn;
    @FXML private TableColumn<Equipment, String> statusColumn;
    @FXML private TableColumn<Equipment, String> termColumn;
    @FXML private TableColumn<Equipment, String> priceColumn;
    @FXML private TextField addModelField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private ComboBox<Integer> rentHoursCombo;
    @FXML private Button rentButton;
    @FXML private Button cancelRentButton;

    private final ObservableList<String> statuses = FXCollections.observableArrayList("Свободно", "В аренде");
    private final ObservableList<Equipment> items = FXCollections.observableArrayList();
    private boolean isAdmin = false;
    private final EquipmentRepository repository = new EquipmentRepository();
    /**
     * Логический тип оборудования для данного экземпляра таблицы
     * (например, "console", "game", "gamepad").
     * Если null, таблица показывает всё оборудование.
     */
    private String equipmentType;

    @FXML
    public void initialize() {
        setUser(Session.getInstance().getUser());
        modelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModel()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        termColumn.setCellValueFactory(cellData -> {
            var rentUntil = cellData.getValue().getRentUntil();
            if (rentUntil == null) return new SimpleStringProperty("");
            var formatted = rentUntil.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            return new SimpleStringProperty("Аренда до: " + formatted);
        });
        priceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPricePerHour())));


        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statuses));
        statusColumn.setOnEditCommit(event -> {
            var equipment = event.getRowValue();
            equipment.setStatus(event.getNewValue());
            // сохраняем изменение
            repository.update(equipment);
            equipmentTable.refresh();
        });

        // по умолчанию: загружаем всё оборудование из БД в observable список
        items.setAll(repository.getAll());
        equipmentTable.setItems(items);

        equipmentTable.setRowFactory(_ -> {
            var row = new TableRow<Equipment>();
            row.setOnMouseClicked(event -> {
                if (isAdmin && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && row.isEmpty()) {
                    addNewItem();
                }
            });
            return row;
        });

        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setOnEditCommit(event -> {
            var equipment = event.getRowValue();
            equipment.setModel(event.getNewValue());
            repository.update(equipment);
        });

        termColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        termColumn.setOnEditCommit(_ -> equipmentTable.refresh());

        // цена/час редактируется только админом; простое текстовое представление числа
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setOnEditCommit(event -> {
            var equipment = event.getRowValue();
            try {
                var newPrice = new BigDecimal(event.getNewValue());
                equipment.setPricePerHour(newPrice);
                repository.update(equipment);
            } catch (NumberFormatException e) {
                // игнорируем неверный ввод, откатываем визуально
                equipmentTable.refresh();
            }
        });

        // заполнение списка часов аренды (1..12)
        if (rentHoursCombo != null) {
            rentHoursCombo.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
            rentHoursCombo.setVisible(false); // скрываем по умолчанию
        }
        if (rentButton != null) {
            rentButton.setDisable(true);
        }
        if (cancelRentButton != null) {
            cancelRentButton.setDisable(true);
        }

        equipmentTable.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> updateRentControlsState());
        if (rentHoursCombo != null) {
            rentHoursCombo.valueProperty().addListener((_, _, _) -> updateRentControlsState());
        }

    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        equipmentTable.setEditable(isAdmin);
        statusColumn.setEditable(isAdmin);
        modelColumn.setEditable(isAdmin);
        termColumn.setEditable(isAdmin);
        // скрываем поля добавления/удаления для user
        if (addButton != null) {
            addButton.setVisible(isAdmin);
        }
        if (addModelField != null) {
            addModelField.setVisible(isAdmin);
        }
        if (deleteButton != null) {
            deleteButton.setVisible(isAdmin);
        }
        if (rentButton != null) {
            // аренда доступна всем, но кнопка может блокироваться логикой ниже
            updateRentControlsState();
        }
        if (cancelRentButton != null) {
            updateRentControlsState();
        }
    }

    public void setUser(User user) {
        setAdmin(Role.ADMIN == user.role());
    }

    /**
     * Настраивает таблицу для отображения только определенного типа оборудования.
     * Используется вкладками, такими как консоли/игры/геймпады.
     */
    public void loadEquipmentByType(String type) {
        this.equipmentType = type;
        items.setAll(repository.getByType(type));
    }

    @FXML
    private void onAdd() {
        if (isAdmin && addModelField.getText() != null && !addModelField.getText().isBlank()) {
            var type = equipmentType != null ? equipmentType : "Оборудование";
            var equipment = new Equipment(addModelField.getText(), "Свободно", null, type);
            int id = repository.add(equipment);
            // сохраняем сгенерированный БД id в объекте для будущих обновлений/удалений
            equipment.setId(id);
            items.add(equipment);
            addModelField.clear();
        }
    }

    private void addNewItem() {
        if (isAdmin) {
            var type = equipmentType != null ? equipmentType : "Оборудование";
            var newItem = new Equipment("Новая модель", "Свободно", null, type);
            var id = repository.add(newItem);
            newItem.setId(id);
            items.add(newItem);
            equipmentTable.getSelectionModel().select(newItem);
            equipmentTable.scrollTo(newItem);
        }
    }

    @FXML
    private void onDelete() {
        if (!isAdmin) {
            return;
        }
        var selected = equipmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            repository.delete(selected.getId());
            items.remove(selected);
        }
    }

    @FXML
    private void onRent() {
        var selected = equipmentTable.getSelectionModel().getSelectedItem();
        var hours = rentHoursCombo != null ? rentHoursCombo.getValue() : null;
        if (selected == null || hours == null) {
            return;
        }
        if (!"Свободно".equals(selected.getStatus())) {
            return;
        }

        var pricePerHour = selected.getPricePerHour();
        var total = pricePerHour.multiply(BigDecimal.valueOf(hours));

        selected.setStatus("В аренде");
        var rentUntil = java.time.LocalDateTime.now().plusHours(hours);
        selected.setRentUntil(rentUntil);

        repository.update(selected);
        equipmentTable.refresh();
        updateRentControlsState();

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Аренда оборудования");
        alert.setHeaderText(null);
        alert.setContentText("Оборудование арендовано.\nЦена за час: " + pricePerHour +
                "\nЧасов: " + hours +
                "\nИтого: " + total);
        alert.showAndWait();
    }

    @FXML
    private void onCancelRent() {
        var selected = equipmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        if (!"В аренде".equals(selected.getStatus())) {
            return;
        }

        selected.setStatus("Свободно");
        selected.setRentUntil(null);
        repository.update(selected);
        equipmentTable.refresh();
        updateRentControlsState();
    }

    private void updateRentControlsState() {
        var selected = equipmentTable.getSelectionModel().getSelectedItem();
        var isFree = selected != null && "Свободно".equals(selected.getStatus());
        var isRented = selected != null && "В аренде".equals(selected.getStatus());

        // дропдаун виден и активен только для свободного оборудования
        if (rentHoursCombo != null) {
            rentHoursCombo.setVisible(isFree);
            rentHoursCombo.setDisable(!isFree);
            if (!isFree && rentHoursCombo.getValue() != null) {
                rentHoursCombo.setValue(null); // сбрасываем значение при скрытии
            }
        }

        var hours = rentHoursCombo != null ? rentHoursCombo.getValue() : null;
        boolean canRent = isFree && hours != null;

        if (rentButton != null) {
            rentButton.setDisable(!canRent);
        }
        if (cancelRentButton != null) {
            cancelRentButton.setDisable(!isRented);
        }
    }
}