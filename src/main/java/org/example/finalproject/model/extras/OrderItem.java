package org.example.finalproject.model.extras;

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

    public double calculatePrice() {
        try {
            ObjectPlusPlus[] links = this.getLinks("refersTo");

            if (links != null && links.length > 0) {
                AdditionalService service = (AdditionalService) links[0];
                return this.quantity * service.getPrice();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return 0;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
