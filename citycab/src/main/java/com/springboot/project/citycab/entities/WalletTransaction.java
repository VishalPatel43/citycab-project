package com.springboot.project.citycab.entities;

import com.springboot.project.citycab.entities.enums.TransactionMethod;
import com.springboot.project.citycab.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletTransactionId;

    private Double amount; // each transaction has some amount either credited or debited

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride; // each transaction is associated with a ride

    private String transactionId; // UTR number

    @CreationTimestamp
    private LocalDateTime timeStamp; // time of transaction

    // One Wallet can have multiple transactions
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
