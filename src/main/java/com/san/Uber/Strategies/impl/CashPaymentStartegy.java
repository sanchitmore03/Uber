package com.san.Uber.Strategies.impl;


import com.san.Uber.Repositories.PaymentRepository;
import com.san.Uber.Services.PaymentService;
import com.san.Uber.Services.WalletService;
import com.san.Uber.Strategies.PaymentStrategy;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Payment;
import com.san.Uber.entities.enums.PaymentStatus;
import com.san.Uber.entities.enums.TransactionMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPaymentStartegy implements PaymentStrategy {
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;
    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;
        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null,
                payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
