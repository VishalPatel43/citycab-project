package com.springboot.project.citycab.services;

import com.springboot.project.citycab.constants.enums.PaymentStatus;
import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Ride;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
