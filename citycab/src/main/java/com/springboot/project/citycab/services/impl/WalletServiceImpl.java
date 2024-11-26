package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.TransactionMethod;
import com.springboot.project.citycab.constants.enums.TransactionType;
import com.springboot.project.citycab.dto.WalletDTO;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.Wallet;
import com.springboot.project.citycab.entities.WalletTransaction;
import com.springboot.project.citycab.exceptions.ResourceNotFoundException;
import com.springboot.project.citycab.repositories.WalletRepository;
import com.springboot.project.citycab.services.WalletService;
import com.springboot.project.citycab.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    // Repository
    private final WalletRepository walletRepository;
    // Service
    private final WalletTransactionService walletTransactionService;

    // Mapper
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId,
                                   Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId,
                                        Ride ride, TransactionMethod transactionMethod) {

        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

//        wallet.getTransactions().add(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + walletId));
    }

    @Override
    @Transactional
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with id: " + user.getUserId()));
    }

    @Transactional
    @Override
    public WalletDTO updateWallet(Long walletId, WalletDTO walletDTO) {
        findWalletById(walletId);
        Wallet wallet = modelMapper.map(walletDTO, Wallet.class);
        wallet.setWalletId(walletId);
        return modelMapper.map(walletRepository.save(wallet), WalletDTO.class);
    }

    @Override
    public WalletDTO getWalletById(Long walletId) {
        Wallet wallet = findWalletById(walletId);
        return modelMapper.map(wallet, WalletDTO.class);
    }

    @Override
    public Page<WalletDTO> getAllWallets(PageRequest pageRequest) {
        return walletRepository.findAll(pageRequest)
                .map(wallet -> modelMapper.map(wallet, WalletDTO.class));
    }

}
