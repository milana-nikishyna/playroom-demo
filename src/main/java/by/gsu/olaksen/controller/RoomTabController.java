package by.gsu.olaksen.controller;

import by.gsu.olaksen.db.RoomBookingRepository;
import by.gsu.olaksen.model.RoomBookingSlot;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class RoomTabController {

    @FXML private DatePicker datePicker;
    @FXML private TabPane roomTabPane;
    @FXML private TableView<RoomBookingSlot> roomTable;
    @FXML private TableColumn<RoomBookingSlot, String> roomHourColumn;
    @FXML private TableColumn<RoomBookingSlot, String> roomStatusColumn;

    private final RoomBookingRepository repository = new RoomBookingRepository();

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
        int roomNumber = roomIdx + 1; // комнаты 1..3

        // по умолчанию все слоты свободны
        ObservableList<RoomBookingSlot> slots = generateSlots();

        // загружаем только забронированные часы из БД и помечаем их
        List<String> bookedHours = repository.getBookedHours(roomNumber, date);
        for (RoomBookingSlot slot : slots) {
            if (bookedHours.contains(slot.getHour())) {
                slot.setStatus("Забронировано");
            }
        }

        roomTable.setItems(slots);
    }

    @FXML
    private void onConfirm() {
        RoomBookingSlot selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null && "Свободно".equals(selected.getStatus())) {
            selected.setStatus("Забронировано");
            persistSlotBooking(selected);
            roomTable.refresh();
        }
    }

    @FXML
    private void onCancel() {
        RoomBookingSlot selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null && "Забронировано".equals(selected.getStatus())) {
            selected.setStatus("Свободно");
            removeSlotBooking(selected);
            roomTable.refresh();
        }
    }

    // Сохраняет бронь конкретного слота (создаёт запись в БД только для "Забронировано")
    private void persistSlotBooking(RoomBookingSlot slot) {
        LocalDate date = datePicker.getValue();
        int roomIdx = roomTabPane.getSelectionModel().getSelectedIndex();
        int roomNumber = roomIdx + 1;
        repository.bookSlot(roomNumber, date, slot.getHour());
    }

    // Удаляет бронь конкретного слота (удаляет запись из БД)
    private void removeSlotBooking(RoomBookingSlot slot) {
        LocalDate date = datePicker.getValue();
        int roomIdx = roomTabPane.getSelectionModel().getSelectedIndex();
        int roomNumber = roomIdx + 1;
        repository.cancelSlot(roomNumber, date, slot.getHour());
    }
}
