package org.example.finalproject;

import javafx.application.Application;
import org.example.finalproject.model.Client;
import org.example.finalproject.model.infrastructure.HotelObject;
import org.example.finalproject.model.infrastructure.RoomStandard;
import org.example.finalproject.model.user.Receptionist;

import java.time.LocalDate;

public class Launcher {
    public static void main(String[] args) {
        generateData();
        Application.launch(HelloApplication.class, args);
    }

    private static void generateData() {
        try {
            new Client("1234567890", "Budomax", "Kokosowa 20", "123456789", "budomax@gmail.com");
            new Client("0987654321", "Sylphide", "Poprzeczna 96", "987654321", "sylphide@gmail.com");

            new Receptionist("Jan", "Kowalski", "kowal123", "password", LocalDate.now());

            HotelObject hotelGrand = new HotelObject("Grand Plaza", "Warszawa, Marszałkowska", 4);
            HotelObject hotelSeaside = new HotelObject("Seaside Resort", "Sopot, Plażowa", 3);

            hotelGrand.addHotelRoom(5, 2, 65, 500, 3, RoomStandard.STANDARD);
            hotelGrand.addHotelRoom(12, 3, 54, 700, 4, RoomStandard.PREMIUM);
            hotelGrand.addHotelRoom(1, 1, 34, 400, 1, RoomStandard.STANDARD);
            hotelGrand.addConferenceRoom(7, 6, 98, 700, 50);
            hotelGrand.addConferenceRoom(8, 4, 77, 600, 40);

            hotelSeaside.addHotelRoom(5, 2, 65, 500, 2, RoomStandard.STANDARD);
            hotelSeaside.addHotelRoom(12, 3, 54, 700, 4, RoomStandard.PREMIUM);
            hotelSeaside.addHotelRoom(1, 1, 34, 400, 1, RoomStandard.STANDARD);
            hotelSeaside.addConferenceRoom(7, 6, 98, 700, 50);
            hotelSeaside.addConferenceRoom(8, 4, 77, 600, 40);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while generating data!\n" + e.getMessage());
        }
    }
}
