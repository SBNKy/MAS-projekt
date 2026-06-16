package org.example.finalproject.model.infrastructure;

public enum RoomStandard {
    STANDARD, PREMIUM;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
