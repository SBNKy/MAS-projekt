package org.example.finalproject.model.user;

import org.example.finalproject.model.extras.AdditionalService;
import org.example.finalproject.model.infrastructure.HotelObject;

import java.io.Serial;
import java.time.LocalDate;

public class Manager extends Receptionist {
    @Serial
    private static final long serialVersionUID = -3146547465540729421L;

    public Manager(String name, String surname, String login, String password, LocalDate dateOfEmployment) {
        super(name, surname, login, password, dateOfEmployment);
    }

    public void addHotelObject(HotelObject newHotelObject) {
        throw new UnsupportedOperationException();
    }

    public void updatePricing(AdditionalService service, double newPrice) {
        throw new UnsupportedOperationException();
    }

    public String generateFinancialReport(HotelObject hotelObject) {
        throw new UnsupportedOperationException();
    }

    public String generateObjectReport(HotelObject hotelObject) {
        throw new UnsupportedOperationException();
    }
}
