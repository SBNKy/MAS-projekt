package org.example.finalproject.model.infrastructure;

import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;

public class Room extends ObjectPlusPlus {
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
}
