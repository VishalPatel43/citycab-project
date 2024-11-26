package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.constants.enums.PaymentMethod;
import com.springboot.project.citycab.constants.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long paymentId;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private Double amount;

    private LocalDateTime paymentTime;

    private RideDTO ride;
}
