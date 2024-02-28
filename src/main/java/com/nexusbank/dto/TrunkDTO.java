package com.nexusbank.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TrunkDTO {

    private Long trunkId;
    private AccountDTO account;
    private CustomerDTO customer;
    private String membership;
}
