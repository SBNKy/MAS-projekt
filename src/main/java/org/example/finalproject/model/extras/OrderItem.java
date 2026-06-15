package org.example.finalproject.model.extras;

import org.example.finalproject.model.reservation.Reservation;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;

public class OrderItem extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = 7788370981263219821L;

    private int quantity;
    private String comment;

    public OrderItem(int quantity, String comments, AdditionalService service) {
        super();

        if (service == null) {
            throw new IllegalArgumentException("Service can't be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can't be negative");
        }

        this.quantity = quantity;
        this.comment = comments;
        this.addLink("refersTo", "includedIn", service);
    }

    public Reservation getReservation() {
        try {
            ObjectPlusPlus[] links = getLinks("reservation");

            if (links.length > 0) {
                return (Reservation) links[0];
            }
        } catch (Exception ignored) {
            throw new IllegalStateException("Order item is not assigned to a reservation.");
        }

        throw new IllegalStateException("Order item is not assigned to a reservation.");
    }

    public AdditionalService getAdditionalService() {
        try {
            ObjectPlusPlus[] links = getLinks("refersTo");

            if (links.length > 0) {
                return (AdditionalService) links[0];
            }
        } catch (Exception ignored) {
            throw new IllegalStateException("Order item is not assigned to an additional service.");
        }

        throw new IllegalStateException("Order item is not assigned to an additional service.");
    }

    public double calculatePrice() {
        return this.quantity * getAdditionalService().getPrice();
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
