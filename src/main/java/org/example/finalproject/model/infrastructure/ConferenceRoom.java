package org.example.finalproject.model.infrastructure;

import java.io.Serial;

public class ConferenceRoom extends Room {
    @Serial
    private static final long serialVersionUID = 2051280413754307549L;

    private double hourlyRate;
    private int capacity;

    public ConferenceRoom(int roomNumber, int floor, int area, double hourlyRate, int capacity,
                          HotelObject hotelObject) {
        super(roomNumber, floor, area, hotelObject);
        setHourlyRate(hourlyRate);
        this.capacity = capacity;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double newHourlyRate) {
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate can't be lower than 0.");
        }
        hourlyRate = newHourlyRate;
    }

    public int getCapacity() {
        return capacity;
    }
}
