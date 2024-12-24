package com.san.Uber.Services.impl;

import com.san.Uber.Dto.WalletTransactionDto;
import com.san.Uber.Repositories.WalletTransactionRepository;
import com.san.Uber.Services.WalletTranscationService;
import com.san.Uber.entities.WalletTransaction;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTranscationServiceImpl implements WalletTranscationService {
    private final ModelMapper modelMapper;
    private final WalletTransactionRepository walletTransactionRepository;
    @Override
    public void creatNewWalletTranscation(WalletTransaction walletTransaction) {

        walletTransactionRepository.save(walletTransaction);
    }


}
