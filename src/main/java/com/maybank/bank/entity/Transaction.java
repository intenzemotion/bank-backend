package com.maybank.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private String status; // pending, failure, success

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate modifiedAt;
}
