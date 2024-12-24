package com.san.Uber.Strategies;

import com.san.Uber.Strategies.impl.CashPaymentStartegy;
import com.san.Uber.Strategies.impl.WalletPaymentStrategy;
import com.san.Uber.entities.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PaymentStrategyManager {
    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStartegy cashPaymentStartegy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
        return switch (paymentMethod){
            case WALLET -> walletPaymentStrategy;
            case CASH -> cashPaymentStartegy;
            default -> throw new RuntimeException("Invalid payment method");
        };
    }

}
