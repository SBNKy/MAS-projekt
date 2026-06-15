package org.example.finalproject.model.infrastructure;

import java.io.Serial;

public class HotelRoom extends Room {
    @Serial
    private static final long serialVersionUID = 6499368087113394852L;

    private double dailyRate;
    private int numberOfBeds;
    private RoomStandard roomStandard;

    public HotelRoom(int roomNumber, int floor, int area, double dailyRate, int numberOfBeds,
                     RoomStandard roomStandard, HotelObject hotelObject) {
        super(roomNumber, floor, area, hotelObject);
        this.numberOfBeds = numberOfBeds;
        this.roomStandard = roomStandard;
        setDailyRate(dailyRate);
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double newDailyRate) {
        if (newDailyRate < 0) {
            throw new IllegalArgumentException("Daily rate can't be lower than 0.");
        }

        dailyRate = newDailyRate;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public RoomStandard getRoomStandard() {
        return roomStandard;
    }
}
