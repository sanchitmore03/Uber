package com.san.Uber.Strategies.impl;


import com.san.Uber.Repositories.PaymentRepository;
import com.san.Uber.Services.PaymentService;
import com.san.Uber.Services.WalletService;
import com.san.Uber.Strategies.PaymentStrategy;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Payment;
import com.san.Uber.entities.Rider;
import com.san.Uber.entities.enums.PaymentStatus;
import com.san.Uber.entities.enums.TransactionMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(),
                null,payment.getRide(), TransactionMethod.RIDE);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(),
                driversCut,null,
                payment.getRide(),TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

    }
}
