package org.example.finalproject.model.infrastructure;

import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;

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

    public HotelRoom addHotelRoom(int number, int floor, int area, double dailyRate, int numberOfBeds,
                                  RoomStandard roomStandard) throws Exception {
        HotelRoom room = new HotelRoom(number, floor, area, dailyRate, numberOfBeds, roomStandard, this);
        this.addPart("owns", "is_in", room);

        return room;
    }

    public ConferenceRoom addConferenceRoom(int number, int floor, int area, double hourlyRate, int capacity) throws Exception {
        ConferenceRoom room = new ConferenceRoom(number, floor, area, hourlyRate, capacity, this);
        this.addPart("owns", "is_in", room);

        return room;
    }

    public int calculateOccupany() {
        throw new UnsupportedOperationException();
    }

    public static List<HotelObject> findAvailableHotels(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getStarRating() {
        return starRating;
    }

    @Override
    public void destroy() {
        try {
            ObjectPlusPlus[] rooms = this.getLinks("owns");

            if (rooms != null) {
                for (ObjectPlusPlus obj : rooms) {
                    obj.destroy();
                }
            }
        } catch (Exception ignored) {}

        super.destroy();
    }
}
