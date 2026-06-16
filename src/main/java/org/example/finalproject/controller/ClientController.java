package org.example.finalproject.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.finalproject.model.Client;
import org.example.finalproject.util.ObjectPlus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ClientController {
    @FXML private TextField nipField;
    @FXML private TextField companyNameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private Button createClientButton;

    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, String> nipColumn;
    @FXML private TableColumn<Client, String> companyColumn;
    @FXML private TableColumn<Client, String> phoneColumn;
    @FXML private Button continueButton;

    @FXML
    public void initialize() {
        nipColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNIP()));
        companyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        refreshTable();
    }

    private void refreshTable() {
        try {
            Iterable<Client> clients = ObjectPlus.getExtent(Client.class);
            ObservableList<Client> clientList = FXCollections.observableArrayList();

            for (Client client : clients) {
                clientList.add(client);
            }
            clientsTable.setItems(clientList);
        } catch (ClassNotFoundException e) {
            clientsTable.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleCreateClient() {
        String nip = nipField.getText();
        String companyName = companyNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneField.getText();
        String email = emailField.getText();

        try {
            validateFormFields(nip, companyName, address, phoneNumber, email);

            new Client(nip, companyName, address, phoneNumber, email);

            clearFormFields();
            showAlert(Alert.AlertType.INFORMATION, "Client created", "Client has been created successfully.");
            refreshTable();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid value", e.getMessage());
        }
    }

    private void validateFormFields(String nip, String companyName, String address, String phoneNumber, String email) {
        if (nip == null || nip.isBlank() || !nip.trim().matches("\\d{10}")) {
            throw new IllegalArgumentException("Proper NIP is required.");
        }
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name is required.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required.");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
    }

    private void clearFormFields() {
        nipField.clear();
        companyNameField.clear();
        addressField.clear();
        phoneField.clear();
        emailField.clear();
    }

    @FXML
    private void handleContinue() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.ERROR, "Selection required", "Select a client from the table to continue.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/finalproject/room-selection-view" +
                                                                              ".fxml"));
            Parent root = loader.load();

            ReservationController reservationController = loader.getController();
            reservationController.setClient(selectedClient);

            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 768));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Critical error!", "Couldn't proceed with reservation.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {

    }
}
