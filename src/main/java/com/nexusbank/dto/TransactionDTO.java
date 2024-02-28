package com.nexusbank.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TransactionDTO extends BasicDTO {

    private Long transactionId;
    @NotEmpty
    private String transactionType;
    @NotNull
    private AccountDTO initiatorAccount;
    private AccountDTO receiverAccount;
    @Positive
    private double amount;
    @NotEmpty
    private String currency;
    private String reason;

}
