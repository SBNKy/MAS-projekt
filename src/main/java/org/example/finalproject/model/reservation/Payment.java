package org.example.finalproject.model.reservation;

import org.example.finalproject.util.ObjectPlusPlus;

import java.io.Serial;
import java.time.LocalDate;

public class Payment extends ObjectPlusPlus {
    @Serial
    private static final long serialVersionUID = -527174670073170790L;

    private double amount;
    private LocalDate postingDate;
    private PaymentMethod paymentMethod;

    public Payment(double amount, LocalDate postingDate, PaymentMethod paymentMethod) {
        super();

        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }
        this.amount = amount;
        this.postingDate = postingDate;
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDate postingDate) {
        this.postingDate = postingDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
