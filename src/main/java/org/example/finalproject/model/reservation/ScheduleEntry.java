package org.example.finalproject.model.reservation;

import org.example.finalproject.model.infrastructure.Room;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;

public class ScheduleEntry extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 6370122498101176909L;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    public ScheduleEntry(Reservation reservation, Room room, LocalDate dateFrom, LocalDate dateTo) {
        super();

        validateDateRange(dateFrom, dateTo);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }

        this.dateFrom = dateFrom;
        this.dateTo = dateTo;

        addLink("reservation", "scheduleEntries", reservation);
        addLink("room", "scheduleEntries", room);
    }

    public boolean checkAvailability(LocalDate dateFrom, LocalDate dateTo) {
        return !overlaps(dateFrom, dateTo);
    }

    public Reservation getReservation() {
        try {
            ObjectPlusPlus[] links = getLinks("reservation");

            if (links.length > 0) {
                return (Reservation) links[0];
            }
        } catch (Exception ignored) {
            throw new IllegalStateException("Schedule entry is not assigned to a reservation.");
        }

        throw new IllegalStateException("Schedule entry is not assigned to a reservation.");
    }

    public Room getRoom() {
        try {
            ObjectPlusPlus[] links = getLinks("room");

            if (links.length > 0) {
                return (Room) links[0];
            }
        } catch (Exception ignored) {
            throw new IllegalStateException("Schedule entry is not assigned to a room.");
        }

        throw new IllegalStateException("Schedule entry is not assigned to a room.");
    }

    public boolean overlaps(LocalDate dateFrom, LocalDate dateTo) {
        validateDateRange(dateFrom, dateTo);
        return this.dateFrom.isBefore(dateTo) && dateFrom.isBefore(this.dateTo);
    }

    public static void validateDateRange(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new IllegalArgumentException("Date range cannot contain null values.");
        }
        if (!dateTo.isAfter(dateFrom)) {
            throw new IllegalArgumentException("End date must be after start date.");
        }
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
