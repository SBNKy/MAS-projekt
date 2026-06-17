package org.example.finalproject.model.reservation;

import org.example.finalproject.model.Client;
import org.example.finalproject.model.extras.AdditionalService;
import org.example.finalproject.model.extras.OrderItem;
import org.example.finalproject.model.infrastructure.Room;
import org.example.finalproject.model.user.Receptionist;
import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = -1397821675645257157L;

    private ReservationStatus reservationStatus;
    private LocalDate submissionDate;
    private String invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String comment;

    public Reservation(Client client, Receptionist receptionist, LocalDate submissionDate) {
        super();

        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        if (receptionist == null) {
            throw new IllegalArgumentException("Receptionist cannot be null.");
        }
        if (submissionDate == null) {
            throw new IllegalArgumentException("Submission date cannot be null.");
        }

        this.reservationStatus = ReservationStatus.PENDING;
        this.submissionDate = submissionDate;
        this.invoiceNumber = null;
        this.invoiceCreationDate = null;
        this.comment = "None";

        addLink("client", "reservations", client);
        addLink("receptionist", "handledReservations", receptionist);
    }

    public void reserveRoom(Room room, LocalDate dateFrom, LocalDate dateTo) {
        if (reservationStatus == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot add rooms to a cancelled reservation.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        if (!room.isAvailable(dateFrom, dateTo)) {
            throw new IllegalArgumentException("Room is not available in the selected period.");
        }

        new ScheduleEntry(this, room, dateFrom, dateTo);
    }

    public Payment addPayment(double amount, LocalDate postingDate, PaymentMethod paymentMethod) throws Exception {
        Payment payment = new Payment(amount, postingDate, paymentMethod);
        addPart("payments", "reservation", payment);

        return payment;
    }

    public void addOrderItem(int quantity, AdditionalService service) throws Exception {
        addOrderItem(quantity, "None", service);
    }

    public void addOrderItem(int quantity, String comment, AdditionalService service) throws Exception {
        OrderItem orderItem = new OrderItem(quantity, comment, service);
        addPart("orderItems", "reservation", orderItem);

    }

    public void startStay() {
        if (reservationStatus != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Reservation must be confirmed to start stay.");
        }
        reservationStatus = ReservationStatus.ONGOING;
    }

    public void endStay() {
        if (reservationStatus != ReservationStatus.ONGOING) {
            throw new IllegalStateException("Reservation must be ongoing to end stay.");
        }
        reservationStatus = ReservationStatus.COMPLETED;
    }

    public Client getClient() {
        try {
            ObjectPlusPlus[] links = getLinks("client");

            if (links.length > 0) {
                return (Client) links[0];
            }
        } catch (Exception ignored) {
            throw new IllegalStateException("Missing required link: client");
        }

        throw new IllegalStateException("Missing required link: client");
    }

    public Receptionist getReceptionist() {
        try {
            ObjectPlusPlus[] links = getLinks("receptionist");

            if (links.length > 0) {
                return (Receptionist) links[0];
            }
        } catch (Exception ignored) {
            throw new IllegalStateException("Missing required link: receptionist");
        }

        throw new IllegalStateException("Missing required link: receptionist");
    }

    public List<ScheduleEntry> getScheduleEntries() {
        List<ScheduleEntry> scheduleEntries = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = getLinks("scheduleEntries");

            for (ObjectPlusPlus object : links) {
                scheduleEntries.add((ScheduleEntry) object);
            }
        } catch (Exception ignored) {
            return scheduleEntries;
        }

        return scheduleEntries;
    }

    public List<Payment> getPayments() {
        List<Payment> payments = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = getLinks("payments");

            for (ObjectPlusPlus object : links) {
                payments.add((Payment) object);
            }
        } catch (Exception ignored) {
            return payments;
        }

        return payments;
    }

    public List<OrderItem> getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();

        try {
            ObjectPlusPlus[] links = getLinks("orderItems");

            for (ObjectPlusPlus object : links) {
                orderItems.add((OrderItem) object);
            }
        } catch (Exception ignored) {
            return orderItems;
        }

        return orderItems;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceCreationDate() {
        return invoiceCreationDate;
    }

    public void setInvoiceCreationDate(LocalDate invoiceCreationDate) {
        this.invoiceCreationDate = invoiceCreationDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void destroy() {
        for (Payment payment : getPayments()) {
            payment.destroy();
        }
        for (OrderItem orderItem : getOrderItems()) {
            orderItem.destroy();
        }
        for (ScheduleEntry scheduleEntry : getScheduleEntries()) {
            scheduleEntry.destroy();
        }
        super.destroy();
    }

    @Override
    public String toString() {
        String clientName = "Unknown";
        try {
            clientName = getClient().getCompanyName();
        } catch (Exception ignored) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== RESERVATION ===\n");
        sb.append(String.format("Status: %s | Client: %s | Created: %s\n",
                                getReservationStatus(), clientName, getSubmissionDate()));

        sb.append("Assigned rooms:\n");
        List<ScheduleEntry> entries = getScheduleEntries();

        for (ScheduleEntry entry : entries) {
            try {
                Room room = entry.getRoom();
                String hotelName = "Unknown hotel";
                try {
                    hotelName = room.getHotelObject().getName();
                } catch (Exception ignored) {
                }

                sb.append(String.format("  - Hotel: %s | Room no. %d (Floor: %d) | From: %s To: %s\n",
                                        hotelName, room.getRoomNumber(), room.getFloor(), entry.getDateFrom(),
                                        entry.getDateTo()));
            } catch (Exception e) {
                sb.append("  - Error reading room association\n");
            }
        }


        sb.append("Selected additional services:\n");
        List<OrderItem> items = getOrderItems();
        if (items.isEmpty()) {
            sb.append("  - No additional services\n");
        } else {
            for (OrderItem item : items) {
                try {
                    AdditionalService service = item.getAdditionalService();
                    sb.append(String.format("  - %s | Qty: %d | Unit price: %.2f\n",
                                            service.getServiceName(), item.getQuantity(), service.getPrice()));
                } catch (Exception e) {
                    sb.append("  - Error reading service association\n");
                }
            }
        }
        sb.append("===================\n");

        return sb.toString();
    }
}
