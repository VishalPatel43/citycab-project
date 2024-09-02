package com.springboot.project.citycab.entities;

import com.springboot.project.citycab.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletTransactionId;

    private Double amount; // each transaction has some amount either credited or debited

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @OneToOne
    private Ride ride; // each transaction is associated with a ride

    private String transactionId; // UTR number

    @CreationTimestamp
    private LocalDateTime timeStamp; // time of transaction

    // One Wallet can have multiple transactions
    @ManyToOne
    private Wallet wallet;
}
