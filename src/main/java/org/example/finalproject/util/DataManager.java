package org.example.finalproject.util;

import org.example.finalproject.model.Client;
import org.example.finalproject.model.extras.Catering;
import org.example.finalproject.model.extras.CateringVariant;
import org.example.finalproject.model.extras.MultimediaEquipment;
import org.example.finalproject.model.infrastructure.HotelObject;
import org.example.finalproject.model.infrastructure.RoomStandard;
import org.example.finalproject.model.user.Receptionist;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public final class DataManager {

    private static final String DIR_NAME = "data";
    private static final String DATA_FILE = DIR_NAME + "/data.ser";

    private DataManager() {}

    public static void saveToFile() {
        Path path = Paths.get(DATA_FILE);

        try {
            if (path.getParent() != null && !Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Error: Could not create directory: " + e.getMessage());
            return;
        }

        try (var oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            ObjectPlus.writeExtends(oos);
            System.out.println("File saved!");
        } catch (Exception e) {
            System.err.println("Error: Could not save file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadFromFile() {
        Path path = Paths.get(DATA_FILE);
        if (!Files.exists(path)) {
            System.out.println("File not found! Generating dummy data...");
            generateData();
            return;
        }

        try (var ois  = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            ObjectPlus.readExtents(ois);
            System.out.println("Data read from file successfully!");
        } catch (Exception e) {
            System.err.println("Error: Could not read file: " + e.getMessage());
        }
    }

    private static void generateData() {
        try {
            new Client("1234567890", "Budomax", "Kokosowa 20", "123456789", "budomax@gmail.com");
            new Client("0987654321", "Sylphide", "Poprzeczna 96", "987654321", "sylphide@gmail.com");

            new Receptionist("Jan", "Kowalski", "kowal123", "password", LocalDate.now());

            HotelObject hotelGrand = new HotelObject("Grand Plaza", "Warszawa, Marszałkowska", 4);
            HotelObject hotelSeaside = new HotelObject("Seaside Resort", "Sopot, Plażowa", 3);

            hotelGrand.addHotelRoom(5, 2, 45, 500, 3, RoomStandard.STANDARD);
            hotelGrand.addHotelRoom(12, 3, 54, 700, 4, RoomStandard.PREMIUM);
            hotelGrand.addHotelRoom(1, 1, 34, 400, 1, RoomStandard.STANDARD);
            hotelGrand.addConferenceRoom(7, 6, 98, 700, 50);
            hotelGrand.addConferenceRoom(8, 4, 77, 600, 40);

            hotelSeaside.addHotelRoom(5, 2, 65, 500, 2, RoomStandard.STANDARD);
            hotelSeaside.addHotelRoom(12, 3, 54, 700, 4, RoomStandard.PREMIUM);
            hotelSeaside.addHotelRoom(1, 1, 34, 400, 1, RoomStandard.STANDARD);
            hotelSeaside.addConferenceRoom(7, 6, 98, 700, 50);
            hotelSeaside.addConferenceRoom(8, 4, 77, 600, 40);

            new Catering("Coffee Break", 45.0, CateringVariant.NORMAL, false);
            new Catering("Vegan Lunch", 120.0, CateringVariant.VEGAN, true);
            new Catering("Banquet Dinner", 250.0, CateringVariant.NORMAL, true);
            new Catering("Fruit Basket", 60.0, CateringVariant.VEGAN, false);

            new MultimediaEquipment("4K Projector", 150.0, 100293847L, 500.0);
            new MultimediaEquipment("HD Projector", 80.0, 100293848L, 300.0);
            new MultimediaEquipment("Wireless Mics", 100.0, 556677881L, 200.0);
            new MultimediaEquipment("Sound System", 350.0, 998877665L, 1000.0);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while generating data!\n" + e.getMessage());
        }
    }
}
