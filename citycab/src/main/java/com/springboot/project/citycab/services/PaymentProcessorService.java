package com.springboot.project.citycab.services;

import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.enums.PaymentStatus;

public interface PaymentProcessorService {
    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
