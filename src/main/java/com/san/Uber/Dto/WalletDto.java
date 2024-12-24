package com.san.Uber.Dto;

import com.san.Uber.entities.User;
import com.san.Uber.entities.WalletTransaction;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {
    private Long id;
    private UserDto user;
    private Double balance;
    private List<WalletTransactionDto> transactions;
}
