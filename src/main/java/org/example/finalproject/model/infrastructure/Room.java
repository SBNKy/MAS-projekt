package org.example.finalproject.model.infrastructure;

import org.example.finalproject.model.reservation.ScheduleEntry;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Room extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 3784983895542012228L;

    private int roomNumber;
    private int floor;
    private int area;

    Room(int roomNumber, int floor, int area, HotelObject hotelObject) {
        super();
        if (hotelObject == null) {
            throw new IllegalArgumentException("Room must be a part of Hotel Object.");
        }

        this.roomNumber = roomNumber;
        this.floor = floor;
        this.area = area;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public boolean isAvailable(LocalDate dateFrom, LocalDate dateTo) {
        ScheduleEntry.validateDateRange(dateFrom, dateTo);

        for (ScheduleEntry entry : getScheduleEntries()) {
            if (entry.overlaps(dateFrom, dateTo)) {
                return false;
            }
        }

        return true;
    }

    public HotelObject getHotelObject() {
        try {
            ObjectPlusPlus[] links = getLinks("belongsTo");

            if (links.length > 0) {
                return (HotelObject) links[0];
            }
        } catch (Exception ignored) {
            throw new IllegalStateException("Room is not assigned to a hotel object.");
        }

        throw new IllegalStateException("Room is not assigned to a hotel object.");
    }

    public List<ScheduleEntry> getScheduleEntries() {
        List<ScheduleEntry> entries = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = getLinks("scheduleEntries");

            for (ObjectPlusPlus object : links) {
                entries.add((ScheduleEntry) object);
            }
        } catch (Exception ignored) {
            return entries;
        }

        return entries;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public int getArea() {
        return area;
    }
}
