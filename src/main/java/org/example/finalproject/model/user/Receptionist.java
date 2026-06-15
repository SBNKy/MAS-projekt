package org.example.finalproject.model.user;

import org.example.finalproject.model.Client;
import org.example.finalproject.model.Reservation;
import org.example.finalproject.model.ReservationStatus;
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

    public void changeReservationStatus(Reservation reservation, ReservationStatus reservationStatus) {

    }

    public List<Room> findResources(HotelObject hotelObject) {
        return new ArrayList<>();
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
