package by.gsu.olaksen.controller;

import by.gsu.olaksen.model.RoomBookingSlot;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RoomTabController {

    @FXML private DatePicker datePicker;
    @FXML private TabPane roomTabPane;
    @FXML private TableView<RoomBookingSlot> roomTable;
    @FXML private TableColumn<RoomBookingSlot, String> roomHourColumn;
    @FXML private TableColumn<RoomBookingSlot, String> roomStatusColumn;

    private final Map<LocalDate, ObservableList<RoomBookingSlot>> room1Bookings = new HashMap<>();
    private final Map<LocalDate, ObservableList<RoomBookingSlot>> room2Bookings = new HashMap<>();
    private final Map<LocalDate, ObservableList<RoomBookingSlot>> room3Bookings = new HashMap<>();

    @FXML
    public void initialize() {
        // По умолчанию сегодняшняя дата
        datePicker.setValue(LocalDate.now());

        // Запрет на выбор прошлых дат
        datePicker.setDayCellFactory(_ -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Настройка столбцов
        roomHourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHour()));
        roomStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        // Слушатели на смену даты и комнаты
        datePicker.valueProperty().addListener((_, _, _) -> updateRoomTable());
        roomTabPane.getSelectionModel().selectedIndexProperty().addListener((_, _, _) -> updateRoomTable());

        // Инициализация таблицы
        updateRoomTable();
    }

    // Генерация 12 слотов (10:00–21:00)
    private ObservableList<RoomBookingSlot> generateSlots() {
        ObservableList<RoomBookingSlot> slots = FXCollections.observableArrayList();
        for (int hour = 10; hour < 22; hour++) {
            slots.add(new RoomBookingSlot(String.format("%02d:00", hour), "Свободно"));
        }
        return slots;
    }

    // Обновление таблицы при смене даты/комнаты
    private void updateRoomTable() {
        LocalDate date = datePicker.getValue();
        int roomIdx = roomTabPane.getSelectionModel().getSelectedIndex();
        ObservableList<RoomBookingSlot> slots = switch (roomIdx) {
            case 0 -> room1Bookings.computeIfAbsent(date, _ -> generateSlots());
            case 1 -> room2Bookings.computeIfAbsent(date, _ -> generateSlots());
            case 2 -> room3Bookings.computeIfAbsent(date, _ -> generateSlots());
            default -> FXCollections.observableArrayList();
        };
        roomTable.setItems(slots);
    }

    @FXML
    private void onConfirm() {
        RoomBookingSlot selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null && "Свободно".equals(selected.getStatus())) {
            selected.setStatus("Забронировано");
            roomTable.refresh();
        }
    }

    @FXML
    private void onCancel() {
        RoomBookingSlot selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null && "Забронировано".equals(selected.getStatus())) {
            selected.setStatus("Свободно");
            roomTable.refresh();
        }
    }
}