package com.san.Uber.Dto;


import com.san.Uber.entities.Ride;
import com.san.Uber.entities.Wallet;
import com.san.Uber.entities.enums.TransactionMethod;
import com.san.Uber.entities.enums.TranscationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletTransactionDto {
    private Long id;

    private Double amount;

    private com.san.Uber.entities.enums.TranscationType TranscationType;

    private TransactionMethod transactionMethod;


    private Ride ride;

    private String transcationId;

    private WalletDto wallet;

    private LocalDateTime timestamp;
}
