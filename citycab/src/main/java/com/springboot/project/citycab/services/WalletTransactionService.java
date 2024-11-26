package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.WalletTransactionDTO;
import com.springboot.project.citycab.entities.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface WalletTransactionService {

    void createNewWalletTransaction(WalletTransaction walletTransaction);

    WalletTransaction getWalletTransactionById(Long walletTransactionId);

    WalletTransactionDTO findWalletTransactionById(Long walletTransactionId);

    WalletTransactionDTO updateWalletTransaction(Long walletTransactionId, WalletTransactionDTO walletTransactionDTO);

    Page<WalletTransactionDTO> getAllWalletTransactions(PageRequest pageRequest);

}
