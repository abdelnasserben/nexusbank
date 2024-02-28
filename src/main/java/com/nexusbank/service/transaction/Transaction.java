package com.nexusbank.service.transaction;

import com.nexusbank.constant.TransactionType;
import com.nexusbank.dto.TransactionDTO;
import com.nexusbank.service.account.AccountOperationService;
import com.nexusbank.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Transaction {

    @Autowired
    protected TransactionService transactionService;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected AccountOperationService accountOperationService;

    abstract void init(TransactionDTO transactionDTO);

    abstract TransactionType getType();
}
