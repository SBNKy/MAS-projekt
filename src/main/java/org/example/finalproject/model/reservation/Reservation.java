package org.example.finalproject.model.reservation;

import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;

public class Reservation extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = -1397821675645257157L;

    private Reservation reservationStatus;
    private LocalDate submissionDate;
    private String invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String comment;

    public Reservation(Reservation reservationStatus, LocalDate submissionDate) {
        super();
        this.reservationStatus = reservationStatus;
        this.submissionDate = submissionDate;
        this.invoiceNumber = null;
        this.invoiceCreationDate = null;
        this.comment = "None";

        this.addLink();
    }
}
