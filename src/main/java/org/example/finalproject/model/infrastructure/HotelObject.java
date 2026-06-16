package org.example.finalproject.model.infrastructure;

import org.example.finalproject.model.reservation.ScheduleEntry;
import org.example.finalproject.util.ObjectPlus;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
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
        setStarRating(starRating);
    }

    public HotelRoom addHotelRoom(int number, int floor, int area, double dailyRate, int numberOfBeds,
                                  RoomStandard roomStandard) throws Exception {
        HotelRoom room = new HotelRoom(number, floor, area, dailyRate, numberOfBeds, roomStandard, this);
        this.addPart("owns", "belongsTo", room);

        return room;
    }

    public ConferenceRoom addConferenceRoom(int number, int floor, int area, double hourlyRate, int capacity) throws Exception {
        ConferenceRoom room = new ConferenceRoom(number, floor, area, hourlyRate, capacity, this);
        this.addPart("owns", "belongsTo", room);

        return room;
    }

    public int calculateOccupany() {
        throw new UnsupportedOperationException();
    }

    public static List<HotelObject> findAvailableHotels(LocalDate from, LocalDate to) {
        ScheduleEntry.validateDateRange(from, to);

        List<HotelObject> availableHotels = new ArrayList<>();

        try {
            for (HotelObject hotelObject : ObjectPlus.getExtent(HotelObject.class)) {
                if (!hotelObject.findAvailableRooms(from, to).isEmpty()) {
                    availableHotels.add(hotelObject);
                }
            }
        } catch (ClassNotFoundException ignored) {
            return availableHotels;
        }

        return availableHotels;
    }

    public List<Room> findAvailableRooms(LocalDate from, LocalDate to) {
        ScheduleEntry.validateDateRange(from, to);

        List<Room> availableRooms = new ArrayList<>();
        for (Room room : getRooms()) {
            if (room.isAvailable(from, to)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = getLinks("owns");

            for (ObjectPlusPlus object : links) {
                rooms.add((Room) object);
            }
        } catch (Exception ignored) {
            return rooms;
        }

        return rooms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        if (starRating < 1 || starRating > 5) {
            throw new IllegalArgumentException("Star rating must be between 1 and 5.");
        }

        this.starRating = starRating;
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
