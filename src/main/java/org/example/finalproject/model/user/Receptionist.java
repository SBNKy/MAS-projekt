package org.example.finalproject.model.user;

import org.example.finalproject.model.Client;
import org.example.finalproject.model.reservation.Reservation;
import org.example.finalproject.model.reservation.ReservationStatus;
import org.example.finalproject.model.reservation.ScheduleEntry;
import org.example.finalproject.model.infrastructure.HotelObject;
import org.example.finalproject.model.infrastructure.Room;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receptionist extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = -1380618885863267237L;

    private String name;
    private String surname;
    private String login;
    private String password;
    private LocalDate dateOfEmployment;

    public Receptionist(String name, String surname, String login, String password, LocalDate dateOfEmployment) {
        super();
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.dateOfEmployment = dateOfEmployment;
    }

    public void registerClient(Client client) {
        throw new UnsupportedOperationException();
    }

    public void organizeStay(Client client) {

    }

    public Reservation organizeStay(Client client, List<Room> rooms, LocalDate dateFrom, LocalDate dateTo) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        if (rooms == null || rooms.isEmpty()) {
            throw new IllegalArgumentException("At least one room must be selected.");
        }

        ScheduleEntry.validateDateRange(dateFrom, dateTo);

        for (Room room : rooms) {
            if (room == null) {
                throw new IllegalArgumentException("Selected rooms cannot contain null values.");
            }
            if (!room.isAvailable(dateFrom, dateTo)) {
                throw new IllegalArgumentException("Selected room is not available in the selected period.");
            }
        }

        Reservation reservation = new Reservation(client, this, LocalDate.now());

        for (Room room : rooms) {
            reservation.reserveRoom(room, dateFrom, dateTo);
        }

        return reservation;
    }

    public void changeReservationStatus(Reservation reservation, ReservationStatus reservationStatus) {

    }

    public List<Room> findResources(HotelObject hotelObject) {
        if (hotelObject == null) {
            throw new IllegalArgumentException("Hotel object cannot be null.");
        }

        List<Room> rooms = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = hotelObject.getLinks("owns");

            for (ObjectPlusPlus object : links) {
                rooms.add((Room) object);
            }
        } catch (Exception ignored) {
            return rooms;
        }

        return rooms;
    }

    public List<Room> findResources(HotelObject hotelObject, LocalDate dateFrom, LocalDate dateTo) {
        if (hotelObject == null) {
            throw new IllegalArgumentException("Hotel object cannot be null.");
        }

        return hotelObject.findAvailableRooms(dateFrom, dateTo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }
}
