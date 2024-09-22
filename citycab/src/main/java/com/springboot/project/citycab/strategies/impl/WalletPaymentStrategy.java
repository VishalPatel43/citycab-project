package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.entities.enums.PaymentStatus;
import com.springboot.project.citycab.entities.enums.TransactionMethod;
import com.springboot.project.citycab.repositories.PaymentRepository;
import com.springboot.project.citycab.services.WalletService;
import com.springboot.project.citycab.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(),
                driversCut, null, payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
