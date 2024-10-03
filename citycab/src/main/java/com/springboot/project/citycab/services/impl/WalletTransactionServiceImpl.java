package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.entities.WalletTransaction;
import com.springboot.project.citycab.repositories.WalletTransactionRepository;
import com.springboot.project.citycab.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    // Repository
    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    @Transactional
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }
}
