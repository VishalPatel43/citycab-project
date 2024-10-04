package com.springboot.project.citycab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {

    private Long walletId;

    private UserDTO user;

    private Double balance;

    private List<WalletTransactionDTO> transactions;
}
