package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.Wallet;
import com.springboot.project.citycab.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionDTO {

    private Long walletTransactionId;

    private Double amount;

    private TransactionType transactionType;

    private RideDTO ride;

    private String transactionId;

    private LocalDateTime timeStamp;

    private WalletDTO wallet;
}
