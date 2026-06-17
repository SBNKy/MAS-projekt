package org.example.finalproject.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.finalproject.model.Client;
import org.example.finalproject.model.extras.AdditionalServiceDTO;
import org.example.finalproject.model.infrastructure.ConferenceRoom;
import org.example.finalproject.model.infrastructure.HotelObject;
import org.example.finalproject.model.infrastructure.HotelRoom;
import org.example.finalproject.model.infrastructure.Room;
import org.example.finalproject.model.reservation.Reservation;
import org.example.finalproject.model.user.Receptionist;
import org.example.finalproject.util.ObjectPlus;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private List<AdditionalServiceDTO> selectedServices = new ArrayList<>();

    @FXML
    public void initialize() {
        initHotelTable();
        initRoomTable();
    }

    private void initHotelTable() {
        hotelNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        hotelAddressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        hotelStarsColumn.setCellValueFactory(
                cellData -> new SimpleObjectProperty<>(cellData.getValue().getStarRating()));

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
            throw new IllegalArgumentException("\"From\" date cannot be in the past.");
        }
        if (!to.isAfter(today)) {
            throw new IllegalArgumentException("\"To\" date must be after today.");
        }
        if (!to.isAfter(from)) {
            throw new IllegalArgumentException("\"To\" date must be after \"From\" date.");
        }
    }

    @FXML
    private void handleAdditionalServices() {
        boolean anyRoomSelected = roomSelectionMap.values().stream().anyMatch(BooleanProperty::get);
        if (!anyRoomSelected) {
            showAdditionalServicesError("At least one room has to be selected in order to add additional services.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/finalproject/additional-services-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Additional Services");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(additionalServicesButton.getScene().getWindow());
            stage.showAndWait();

            AdditionalServicesController controller = loader.getController();
            if (controller.isConfirmed()) {
                selectedServices = controller.getChosenServices();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAdditionalServicesError("Unable to load additional services view.");
        }
    }

    @FXML
    private void handleCancel() {
        System.out.println("Cancel button pressed");
    }

    @FXML
    private void handleContinue() {
        List<Room> selectedRooms = new ArrayList<>();
        for (var entry : roomSelectionMap.entrySet()) {
            if (entry.getValue().get()) {
                selectedRooms.add(entry.getKey());
            }
        }

        if (selectedRooms.isEmpty()) {
            showReservationError("At least one room has to be selected in order to create reservation.");
            return;
        }

        LocalDate from = dateFromPicker.getValue();
        LocalDate to = dateToPicker.getValue();
        try {
            validateDates(from, to);

            Receptionist receptionist = null;
            Iterable<Receptionist> receptionists = ObjectPlus.getExtent(Receptionist.class);
            if (receptionists.iterator().hasNext()) {
                receptionist = receptionists.iterator().next();
            }

            if (receptionist == null) {
                showReservationError("Critical error: No receptionist available in the system.");
                return;
            }

            Reservation reservation = receptionist.organizeStay(selectedClient, selectedRooms, from, to);

            if (selectedServices != null && !selectedServices.isEmpty()) {
                for (AdditionalServiceDTO dto : selectedServices) {
                    reservation.addOrderItem(dto.quantity(), dto.service());
                }
            }

            showSuccessAlert();

            printDebugInfo();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/finalproject/reservation-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 768));
            stage.show();

        } catch (IllegalArgumentException e) {
            showReservationError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showReservationError("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showFilterError(String message) {
        showError("Filtering error!", message);
    }

    private void showAdditionalServicesError(String message) {
        showError("Additional services error!", message);
    }

    private void showReservationError(String message) {
        showError("Reservation error!", message);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("Reservation has been successfully created.");
        alert.showAndWait();
    }

    private void printDebugInfo() {
        System.out.println("\n--- RESERVATION EXTENT ---");
        try {
            ObjectPlus.showExtent(Reservation.class);
        } catch (Exception e) {
            System.out.println("Extent is empty or an error occured: " + e.getMessage());
        }
        System.out.println("------------------------------------------\n");
    }
}
