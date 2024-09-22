package com.springboot.project.citycab.strategies;

import com.springboot.project.citycab.entities.Payment;

public interface PaymentStrategy {
    Double PLATFORM_COMMISSION = 0.3;

    void processPayment(Payment payment);

}
