package com.san.Uber.Strategies;

import com.san.Uber.entities.Payment;

public interface PaymentStrategy {
    static final Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
