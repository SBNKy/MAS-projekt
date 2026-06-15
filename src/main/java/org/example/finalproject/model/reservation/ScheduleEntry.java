package org.example.finalproject.model.reservation;

import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScheduleEntry extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 6370122498101176909L;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    public ScheduleEntry(LocalDate dateFrom, LocalDate dateTo) {
        super();

        if (dateTo.isBefore(dateFrom)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public boolean checkAvailability(LocalDate dateFrom, LocalDate dateTo) {
        return this.dateTo.isBefore(dateFrom) || this.dateFrom.isBefore(dateTo);
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
