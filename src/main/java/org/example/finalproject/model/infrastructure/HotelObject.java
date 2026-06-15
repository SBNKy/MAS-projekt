package org.example.finalproject.model.infrastructure;

import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;

public class HotelObject extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 4189076975142036128L;

    private String name;
    private String address;
    private int starRating;

    public HotelObject(String name, String address, int starRating) {
        this.name = name;
        this.address = address;

        if (starRating < 1 || starRating > 5) {
            throw new IllegalArgumentException("Star rating must be between 1 and 5.");
        }
        this.starRating = starRating;
    }

    public HotelRoom addHotelRoom(int number, int floor, int area, double dailyRate, int numberOfBeds, RoomStandard roomStandard) {
        HotelRoom room = new HotelRoom(number, floor, area, dailyRate, numberOfBeds, roomStandard, this);
        this.addLink("owns", "is_in", room);
        return room;
    }
}
