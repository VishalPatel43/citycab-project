package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.WalletTransaction;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
