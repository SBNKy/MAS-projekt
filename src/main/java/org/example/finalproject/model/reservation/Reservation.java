package org.example.finalproject.model.reservation;

import org.example.finalproject.model.Client;
import org.example.finalproject.model.infrastructure.Room;
import org.example.finalproject.model.user.Receptionist;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;

public class Reservation extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = -1397821675645257157L;

    private ReservationStatus reservationStatus;
    private LocalDate submissionDate;
    private String invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String comment;

    public Reservation(Client client, Receptionist receptionist, LocalDate submissionDate) {
        super();

        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        if (receptionist == null) {
            throw new IllegalArgumentException("Receptionist cannot be null.");
        }
        if (submissionDate == null) {
            throw new IllegalArgumentException("Submission date cannot be null.");
        }

        this.reservationStatus = ReservationStatus.PENDING;
        this.submissionDate = submissionDate;
        this.invoiceNumber = null;
        this.invoiceCreationDate = null;
        this.comment = "None";

        addLink("client", "reservations", client);
        addLink("receptionist", "handledReservations", receptionist);
    }

    public ScheduleEntry reserveRoom(Room room, LocalDate dateFrom, LocalDate dateTo) {
        if (reservationStatus == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot add rooms to a cancelled reservation.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        if (!room.isAvailable(dateFrom, dateTo)) {
            throw new IllegalArgumentException("Room is not available in the selected period.");
        }

        return new ScheduleEntry(this, room, dateFrom, dateTo);
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDate getInvoiceCreationDate() {
        return invoiceCreationDate;
    }

    public String getComment() {
        return comment;
    }
}
