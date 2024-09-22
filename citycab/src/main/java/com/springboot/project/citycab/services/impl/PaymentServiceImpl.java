package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.enums.PaymentStatus;
import com.springboot.project.citycab.services.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public void processPayment(Ride ride) {

    }

    @Override
    public Payment createNewPayment(Ride ride) {
        return null;
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {

    }
}
