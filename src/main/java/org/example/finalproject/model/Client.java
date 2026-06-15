package org.example.finalproject.model;

import org.example.finalproject.model.reservation.Reservation;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class Client extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 7766417013780673314L;

    private String NIP;
    private String companyName;
    private String address;
    private String phoneNumber;
    private String email;

    public Client(String NIP, String companyName, String address, String phoneNumber, String email) {
        super();
        this.NIP = NIP;
        this.companyName = companyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getNIP() {
        return NIP;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = getLinks("reservations");

            for (ObjectPlusPlus object : links) {
                reservations.add((Reservation) object);
            }
        } catch (Exception ignored) {
            return reservations;
        }

        return reservations;
    }
}
