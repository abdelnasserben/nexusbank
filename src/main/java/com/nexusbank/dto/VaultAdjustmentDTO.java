package com.nexusbank.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class VaultAdjustmentDTO {

    @NotEmpty
    private String accountName;
    private double currentBalance;
    @Positive
    private double amount;
    @NotEmpty
    private String operationType;
    @NotNull
    private AccountDTO account;
}
