package org.example.finalproject.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.finalproject.model.extras.AdditionalService;
import org.example.finalproject.model.extras.AdditionalServiceDTO;
import org.example.finalproject.model.extras.Catering;
import org.example.finalproject.model.extras.MultimediaEquipment;
import org.example.finalproject.util.ObjectPlus;

import java.util.ArrayList;
import java.util.List;

public class AdditionalServicesController {

    @FXML private TableView<AdditionalService> availableServicesTable;
    @FXML private TableColumn<AdditionalService, String> serviceTypeColumn;
    @FXML private TableColumn<AdditionalService, String> serviceNameColumn;
    @FXML private TableColumn<AdditionalService, Double> servicePriceColumn;

    @FXML private TableView<AdditionalServiceDTO> selectedServicesTable;
    @FXML private TableColumn<AdditionalServiceDTO, String> selectedTypeColumn;
    @FXML private TableColumn<AdditionalServiceDTO, String> selectedNameColumn;
    @FXML private TableColumn<AdditionalServiceDTO, Integer> selectedQuantityColumn;
    @FXML private TableColumn<AdditionalServiceDTO, String> selectedPriceColumn;

    @FXML private TextField quantityField;
    @FXML private Button addButton;

    @FXML private Button cancelButton;
    @FXML private Button saveButton;

    private ObservableList<AdditionalServiceDTO> chosenServicesList = FXCollections.observableArrayList();
    private boolean isConfirmed = false;

    @FXML
    public void initialize() {
        initAvailableTable();
        initSelectedTable();
        loadAvailableServices();
    }

    private void initAvailableTable() {
        serviceTypeColumn.setCellValueFactory(cellData -> {
            AdditionalService service = cellData.getValue();
            if (service instanceof Catering) return new SimpleStringProperty("Catering");
            if (service instanceof MultimediaEquipment) return new SimpleStringProperty("Multimedia Equipment");
            return new SimpleStringProperty("Other");
        });

        serviceNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiceName()));
        servicePriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
    }

    private void initSelectedTable() {
        selectedTypeColumn.setCellValueFactory(cellData -> {
            AdditionalService service = cellData.getValue().service();
            if (service instanceof Catering) return new SimpleStringProperty("Catering");
            if (service instanceof MultimediaEquipment) return new SimpleStringProperty("Multimedia Equipment");
            return new SimpleStringProperty("Other");
        });
        selectedNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().service().getServiceName()));
        selectedQuantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().quantity()));
        selectedPriceColumn.setCellValueFactory(cellData -> {
            double total = cellData.getValue().service().getPrice() * cellData.getValue().quantity();
            return new SimpleStringProperty(String.format("%.2f", total));
        });

        selectedServicesTable.setItems(chosenServicesList);
    }

    private void loadAvailableServices() {
        ObservableList<AdditionalService> availableServices = availableServicesTable.getItems();

        try {
            Iterable<Catering> caterings = ObjectPlus.getExtent(Catering.class);
            for (Catering c : caterings) {
                availableServices.add(c);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("No data in Catering extent");
        }

        try {
            Iterable<MultimediaEquipment> multimedia = ObjectPlus.getExtent(MultimediaEquipment.class);
            for (MultimediaEquipment m : multimedia) {
                availableServices.add(m);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("No data in Multimedia extent");
        }

        availableServicesTable.setItems(availableServices);
    }


    @FXML
    private void handleAddService() {
        AdditionalService selectedService = availableServicesTable.getSelectionModel().getSelectedItem();
        if (selectedService == null) {
            showError("Please select a service.");
            return;
        }

        String quantityText = quantityField.getText();
        if (quantityText == null || quantityText.isEmpty()) {
            showError("Please enter a valid quantity.");
            return;
        }
        if (!quantityText.matches("^(?:[1-9]|10)$")) {
            showError("Quantity has to be between 1 and 10.");
            return;
        }

        int quantity = Integer.parseInt(quantityText);
        for (int i = 0; i < chosenServicesList.size(); i++) {
            AdditionalServiceDTO existingDTO = chosenServicesList.get(i);
            if (existingDTO.service().equals(selectedService)) {
                int qty =  existingDTO.quantity() + quantity;
                chosenServicesList.set(i, new AdditionalServiceDTO(selectedService, Math.min(qty, 10)));
                quantityField.clear();
                return;
            }
        }

        chosenServicesList.add(new AdditionalServiceDTO(selectedService, quantity));
        quantityField.clear();
    }

    @FXML
    private void handleSaveAndClose() {
        isConfirmed = true;
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        isConfirmed = false;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public List<AdditionalServiceDTO> getChosenServices() {
        return new ArrayList<>(chosenServicesList);
    }
}
