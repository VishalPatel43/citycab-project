package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.enums.PaymentStatus;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.PaymentRepository;
import com.springboot.project.citycab.services.PaymentService;
import com.springboot.project.citycab.strategies.PaymentStrategy;
import com.springboot.project.citycab.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride with id: " + ride.getRideId()));

        PaymentStrategy paymentStrategy = paymentStrategyManager.paymentStrategy(payment.getPaymentMethod());
        paymentStrategy.processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .ride(ride)
                .paymentMethod(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
