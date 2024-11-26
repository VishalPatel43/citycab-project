package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.PaymentStatus;
import com.springboot.project.citycab.dto.PaymentDTO;
import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.PaymentRepository;
import com.springboot.project.citycab.services.PaymentService;
import com.springboot.project.citycab.strategies.PaymentStrategy;
import com.springboot.project.citycab.strategies.manager.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentStrategyManager paymentStrategyManager;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride with id: " + ride.getRideId()));

        PaymentStrategy paymentStrategy = paymentStrategyManager.paymentStrategy(payment.getPaymentMethod());
        paymentStrategy.processPayment(payment);
    }

    @Override
    @Transactional
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
    public Payment findPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
    }

    @Override
    public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = findPaymentById(paymentId);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    @Override
    public Page<PaymentDTO> getAllPayments(PageRequest pageRequest) {
        Page<Payment> payments = paymentRepository.findAll(pageRequest);
        return payments.map(payment -> modelMapper.map(payment, PaymentDTO.class));
    }

    @Transactional
    @Override
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) {
        findPaymentById(paymentId);
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setPaymentId(paymentId);
        return modelMapper.map(paymentRepository.save(payment), PaymentDTO.class);
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
