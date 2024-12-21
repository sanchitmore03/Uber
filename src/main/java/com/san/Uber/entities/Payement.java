package com.san.Uber.entities;

import com.san.Uber.entities.enums.PayementMethod;
import com.san.Uber.entities.enums.PayementStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Payement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayementMethod payementMethod;

    @OneToOne(fetch = FetchType.LAZY)
    private Ride ride;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PayementStatus payementStatus;

    @CreationTimestamp
    private LocalDateTime timeStamp;

}
