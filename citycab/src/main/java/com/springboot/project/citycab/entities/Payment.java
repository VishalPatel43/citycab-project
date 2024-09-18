package com.springboot.project.citycab.entities;

import com.springboot.project.citycab.entities.enums.PaymentMethod;
import com.springboot.project.citycab.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Double amount;

    @CreationTimestamp
    private LocalDateTime paymentTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Ride ride;

}
