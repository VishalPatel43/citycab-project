package com.springboot.project.citycab.services;

import com.springboot.project.citycab.constants.enums.PaymentStatus;
import com.springboot.project.citycab.dto.PaymentDTO;
import com.springboot.project.citycab.entities.Payment;
import com.springboot.project.citycab.entities.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    Payment findPaymentById(Long paymentId);

    PaymentDTO getPaymentById(Long paymentId);

    Page<PaymentDTO> getAllPayments(PageRequest pageRequest);

    PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
