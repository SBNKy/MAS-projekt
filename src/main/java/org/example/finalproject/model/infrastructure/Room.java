package org.example.finalproject.model.infrastructure;

import org.example.finalproject.model.reservation.ScheduleEntry;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;

public abstract class Room extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 3784983895542012228L;

    public static final String ROLE_SCHEDULE_ENTRIES = "scheduleEntries";

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

        try {
            ObjectPlusPlus[] entries = getLinks(ROLE_SCHEDULE_ENTRIES);

            for (ObjectPlusPlus object : entries) {
                ScheduleEntry entry = (ScheduleEntry) object;

                if (entry.overlaps(dateFrom, dateTo)) {
                    return false;
                }
            }
        } catch (Exception ignored) {
            return true;
        }

        return true;
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
