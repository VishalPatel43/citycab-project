package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.constants.enums.PaymentStatus;
import com.springboot.project.citycab.constants.enums.TransactionMethod;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Rider;
import com.springboot.project.citycab.services.PaymentService;
import com.springboot.project.citycab.services.WalletService;
import com.springboot.project.citycab.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Rider has some minimum amount in the wallet
// Rider -> 200
// Driver -> 1000 (Driver has some amount in the wallet)
// If ride price and wallet amount is greater than or equal to the ride price then we can process the payment
// If ride price is greater than the wallet amount then we can't process the payment
// Ride cost --> 100, commission 30% -> 30
// Rider -> 200 - 100 = 100
// Driver -> 1000 + (100 - 30) = 1070


@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private PaymentService paymentService;
//    private final PaymentProcessorService paymentProcessorService;

    @Autowired
    public void setPaymentService(@Lazy PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(),
                driversCut, null, payment.getRide(), TransactionMethod.RIDE);

        // TODO: add money to the platform wallet

        paymentService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);
    }
}
