package com.san.Uber.Services.impl;

import com.san.Uber.Dto.RiderDto;
import com.san.Uber.Dto.WalletTransactionDto;
import com.san.Uber.Repositories.WalletRepository;
import com.san.Uber.Services.WalletService;
import com.san.Uber.Services.WalletTranscationService;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.User;
import com.san.Uber.entities.Wallet;
import com.san.Uber.entities.WalletTransaction;
import com.san.Uber.entities.enums.TransactionMethod;
import com.san.Uber.entities.enums.TranscationType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final WalletTranscationService walletTranscationService;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount , String transcationId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance()+amount);
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transcationId(transcationId)
                .ride(ride)
                .wallet(wallet)
                .TranscationType(TranscationType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTranscationService.creatNewWalletTranscation(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount , String transcationId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance()-amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transcationId(transcationId)
                .ride(ride)
                .wallet(wallet)
                .TranscationType(TranscationType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

//        walletTranscationService.creatNewWalletTranscation(walletTransaction);
        wallet.getTransactions().add(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(() ->
                new RuntimeException("Wallet not found with id "+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {

        return walletRepository.findByUser(user).orElseThrow(()->
                new RuntimeException("user not found with id "+user.getId()));
    }
}
