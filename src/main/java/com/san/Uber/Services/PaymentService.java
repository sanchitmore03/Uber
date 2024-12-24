package com.san.Uber.Services;

import com.san.Uber.entities.Payment;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.enums.PaymentStatus;

public interface PaymentService {
    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);
    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
