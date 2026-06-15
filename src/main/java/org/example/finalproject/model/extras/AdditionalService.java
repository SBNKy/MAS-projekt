package org.example.finalproject.model.extras;

import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public abstract class AdditionalService extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 1677651357317429058L;

    private String serviceName;
    private double price;

    public AdditionalService(String serviceName, double price) {
        super();
        this.serviceName = serviceName;
        setPrice(price);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.price = price;
    }

    public List<OrderItem> getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = getLinks("includedIn");

            for (ObjectPlusPlus object : links) {
                orderItems.add((OrderItem) object);
            }
        } catch (Exception ignored) {
            return orderItems;
        }

        return orderItems;
    }
}
