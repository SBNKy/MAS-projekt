package org.example.finalproject.model.extras;

import java.io.Serial;

public class MultimediaEquipment extends AdditionalService {
    @Serial
    private static final long serialVersionUID = 5225671889041656776L;

    private long serialNumber;
    private double deposit;

    public MultimediaEquipment(String serviceName, double price, long serialNumber, double deposit) {
        super(serviceName, price);
        this.serialNumber = serialNumber;
        this.deposit = deposit;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
}
