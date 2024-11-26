package com.springboot.project.citycab.services;

import com.springboot.project.citycab.constants.enums.TransactionMethod;
import com.springboot.project.citycab.dto.WalletDTO;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount,
                            String transactionId, Ride ride,
                            TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount,
                                 String transactionId, Ride ride,
                                 TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);

    WalletDTO updateWallet(Long walletId, WalletDTO walletDTO);

    WalletDTO getWalletById(Long walletId);

    Page<WalletDTO> getAllWallets(PageRequest pageRequest);

}
