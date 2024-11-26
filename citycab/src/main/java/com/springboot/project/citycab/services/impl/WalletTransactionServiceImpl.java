package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.WalletTransactionDTO;
import com.springboot.project.citycab.entities.WalletTransaction;
import com.springboot.project.citycab.repositories.WalletTransactionRepository;
import com.springboot.project.citycab.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    // Repository
    private final WalletTransactionRepository walletTransactionRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }

    @Override
    public WalletTransaction getWalletTransactionById(Long walletTransactionId) {
        return walletTransactionRepository.findById(walletTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet Transaction not found with id: " + walletTransactionId));
    }

    @Override
    public WalletTransactionDTO findWalletTransactionById(Long walletTransactionId) {
        WalletTransaction walletTransaction = getWalletTransactionById(walletTransactionId);
        return modelMapper.map(walletTransaction, WalletTransactionDTO.class);
    }

    @Transactional
    @Override
    public WalletTransactionDTO updateWalletTransaction(Long walletTransactionId, WalletTransactionDTO walletTransactionDTO) {
        getWalletTransactionById(walletTransactionId);
        WalletTransaction walletTransaction = modelMapper.map(walletTransactionDTO, WalletTransaction.class);
        walletTransaction.setWalletTransactionId(walletTransactionId);
        return modelMapper.map(walletTransactionRepository.save(walletTransaction), WalletTransactionDTO.class);
    }

    @Override
    public Page<WalletTransactionDTO> getAllWalletTransactions(PageRequest pageRequest) {
        return walletTransactionRepository.findAll(pageRequest)
                .map(walletTransaction -> modelMapper.map(walletTransaction, WalletTransactionDTO.class));
    }
}
