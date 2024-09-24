package com.springboot.project.citycab.strategies.impl;

import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.enums.PaymentStatus;
import com.springboot.project.citycab.entities.enums.TransactionMethod;
import com.springboot.project.citycab.services.PaymentProcessorService;
import com.springboot.project.citycab.services.WalletService;
import com.springboot.project.citycab.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Rider -> 100
//Driver -> 70 Deduct 30Rs from Driver's wallet

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;

    private final PaymentProcessorService paymentProcessorService;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;

        // We deduct the platform commission from the driver's wallet
        // rider provide the cash to the driver, so platform the deduct the commission from the driver's wallet

        // Initially driver has some money in wallet if this account below means ex 1000 driver have to add the money to the wallet before starting the ride for cash payment
        // or only accept the wallet payment
        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null,
                payment.getRide(), TransactionMethod.RIDE);

        // TODO: add money to the platform wallet

        paymentProcessorService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);
    }
}

//10 ratingsCount -> 4.0
//new rating 4.6
//updated rating
//new rating 44.6/11 -> 4.05
