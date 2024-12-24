package com.san.Uber.entities;

import com.san.Uber.entities.enums.TransactionMethod;
import com.san.Uber.entities.enums.TranscationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private TranscationType TranscationType;

    private TransactionMethod transactionMethod;

    @OneToOne
    private Ride ride;

    private String transcationId;

    @ManyToOne
    private Wallet wallet;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
