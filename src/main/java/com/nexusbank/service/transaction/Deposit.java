package com.nexusbank.service.transaction;

import com.nexusbank.app.CurrencyExchanger;
import com.nexusbank.constant.TransactionType;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.TransactionDTO;
import org.springframework.stereotype.Service;

@Service
public class Deposit extends Transaction {

    @Override
    public void init(TransactionDTO transactionDTO) {

        //TODO: for deposit, initiator account is the beneficiary account so we interchange
        AccountDTO debitAccount = accountService.findVault(transactionDTO.getBranch(), transactionDTO.getCurrency());
        AccountDTO creditAccount = transactionDTO.getInitiatorAccount();

        //TODO: exchange amount in given currency
        double creditAmount = CurrencyExchanger.exchange(transactionDTO.getCurrency(), creditAccount.getCurrency(), transactionDTO.getAmount());

        accountOperationService.debit(debitAccount, transactionDTO.getAmount());
        accountOperationService.credit(creditAccount, creditAmount);

        //TODO: we set the debit account of transaction
        transactionDTO.setReceiverAccount(debitAccount);

        transactionService.save(transactionDTO);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }
}
