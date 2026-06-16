package org.example.finalproject.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.example.finalproject.model.Client;
import org.example.finalproject.model.infrastructure.ConferenceRoom;
import org.example.finalproject.model.infrastructure.HotelObject;
import org.example.finalproject.model.infrastructure.HotelRoom;
import org.example.finalproject.model.infrastructure.Room;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationController {
    @FXML
    private Label currentClientLabel;

    @FXML
    private DatePicker dateFromPicker;
    @FXML
    private DatePicker dateToPicker;
    @FXML
    private Button filterButton;

    @FXML
    private TableView<HotelObject> hotelsTable;
    @FXML
    private TableColumn<HotelObject, String> hotelNameColumn;
    @FXML
    private TableColumn<HotelObject, String> hotelAddressColumn;
    @FXML
    private TableColumn<HotelObject, Integer> hotelStarsColumn;

    @FXML
    private TableView<Room> roomsTable;
    @FXML
    private TableColumn<Room, Boolean> roomSelectColumn;
    @FXML
    private TableColumn<Room, String> roomTypeColumn;
    @FXML
    private TableColumn<Room, Integer> roomFloorColumn;
    @FXML
    private TableColumn<Room, String> roomStandardColumn;
    @FXML
    private TableColumn<Room, Integer> roomAreaColumn;
    @FXML
    private TableColumn<Room, String> roomPriceColumn;

    @FXML
    private Button additionalServicesButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button continueButton;

    private Client selectedClient;
    private Map<Room, BooleanProperty> roomSelectionMap = new HashMap<>();

    @FXML
    public void initialize() {
        initHotelTable();
        initRoomTable();
    }

    private void initHotelTable() {
        hotelNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        hotelAddressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        hotelStarsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStarRating()));

        hotelsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> loadHotelRooms(newValue)
        );
    }

    private void initRoomTable() {
        roomSelectColumn.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            return roomSelectionMap.computeIfAbsent(room, r -> new SimpleBooleanProperty(false));
        });
        roomSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(roomSelectColumn));

        roomTypeColumn.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            if (room instanceof HotelRoom) return new SimpleStringProperty("Room");
            return new SimpleStringProperty("Conference");
        });
        roomFloorColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFloor()));
        roomStandardColumn.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            if (room instanceof HotelRoom r) return new SimpleStringProperty(r.getRoomStandard().toString());
            return new SimpleStringProperty("-");
        });
        roomAreaColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
        roomPriceColumn.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            if (room instanceof HotelRoom r) return new SimpleStringProperty(
                    String.format("%.2f/day", r.getDailyRate())
            );
            else if (room instanceof ConferenceRoom r) return new SimpleStringProperty(
                    String.format("%.2f/hour", r.getHourlyRate())
            );
            else return new SimpleStringProperty("0");
        });
    }

    public void setClient(Client client) {
        this.selectedClient = client;
        currentClientLabel.setText("Current client: " + client.getCompanyName());
    }

    @FXML
    private void handleFilter() {
        LocalDate from = dateFromPicker.getValue();
        LocalDate to = dateToPicker.getValue();

        try {
            validateDates(from, to);

            List<HotelObject> availableHotels = HotelObject.findAvailableHotels(from, to);
            ObservableList<HotelObject> observableHotels = FXCollections.observableArrayList(availableHotels);

            hotelsTable.setItems(observableHotels);
            roomsTable.setItems(FXCollections.observableArrayList());
            roomSelectionMap.clear();

        } catch (IllegalArgumentException e) {
            showFilterError(e.getMessage());
        }
    }

    private void loadHotelRooms(HotelObject hotelObject) {
        if (hotelObject == null) return;

        LocalDate from = dateFromPicker.getValue();
        LocalDate to = dateToPicker.getValue();

        try {
            validateDates(from, to);

            List<Room> availableRooms = hotelObject.findAvailableRooms(from, to);
            roomsTable.setItems(FXCollections.observableArrayList(availableRooms));
        } catch (IllegalArgumentException e) {
            roomsTable.setItems(FXCollections.observableArrayList());
            showFilterError(e.getMessage());
        }

    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date fields must not be empty.");
        }

        LocalDate today = LocalDate.now();
        if (from.isBefore(today)) {
            throw new IllegalArgumentException("\"From\" date must be after today.");
        }
        if (!to.isAfter(today)) {
            throw new IllegalArgumentException("\"To\" date must be after today.");
        }
        if (!to.isAfter(from)) {
            throw new IllegalArgumentException("\"To\" date must be after \"From\" date.");
        }
    }

    private void showFilterError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Filtering error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAdditionalServices() {
        System.out.println("Additional services button pressed");
    }

    @FXML
    private void handleCancel() {
        System.out.println("Cancel button pressed");
    }

    @FXML
    private void handleContinue() {

    }
}
