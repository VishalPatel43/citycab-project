package com.springboot.project.citycab.entities;

import com.springboot.project.citycab.entities.enums.PaymentMethod;
import com.springboot.project.citycab.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Double amount;

    private LocalDateTime paymentTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Ride ride;

}
