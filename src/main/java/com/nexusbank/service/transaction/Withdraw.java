package com.nexusbank.service.transaction;

import com.nexusbank.app.AccountChecker;
import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.TransactionType;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.TransactionDTO;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.stereotype.Service;

@Service
public class Withdraw extends Transaction {

    @Override
    public void init(TransactionDTO transactionDTO) {

        //TODO: for withdraw, debit account is the initiator account of transaction so we interchange nothing
        AccountDTO debitAccount = transactionDTO.getInitiatorAccount();
        AccountDTO creditAccount = accountService.findVault(transactionDTO.getBranch(), transactionDTO.getCurrency());

        if (!AccountChecker.isActive(debitAccount)) {
            CustomNotification.show("Account must be active", NotificationVariant.LUMO_ERROR);
            return;
        }

        if (debitAccount.getBalance() < transactionDTO.getAmount()) {
            CustomNotification.show("Account balance is insufficient", NotificationVariant.LUMO_ERROR);
            return;
        }

        if (!debitAccount.getCurrency().equals(transactionDTO.getCurrency())) {
            CustomNotification.show("Transaction and account currencies must be the same", NotificationVariant.LUMO_ERROR);
            return;
        }

        accountOperationService.debit(debitAccount, transactionDTO.getAmount());
        accountOperationService.credit(creditAccount, transactionDTO.getAmount());

        //TODO: we set the credit account of transaction
        transactionDTO.setReceiverAccount(creditAccount);

        transactionService.save(transactionDTO);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAW;
    }
}
