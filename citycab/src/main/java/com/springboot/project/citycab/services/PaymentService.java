package com.springboot.project.citycab.services;

import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
