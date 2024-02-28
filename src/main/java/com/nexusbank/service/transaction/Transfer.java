package com.nexusbank.service.transaction;

import com.nexusbank.app.AccountChecker;
import com.nexusbank.app.CurrencyExchanger;
import com.nexusbank.app.CustomNotification;
import com.nexusbank.constant.TransactionType;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.TransactionDTO;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.stereotype.Service;

@Service
public class Transfer extends Transaction {

    @Override
    public void init(TransactionDTO transactionDTO) {

        AccountDTO debitAccount = transactionDTO.getInitiatorAccount();
        AccountDTO creditAccount = transactionDTO.getReceiverAccount();

        if (!AccountChecker.isActive(debitAccount)) {
            CustomNotification.show("Initiator account must be active", NotificationVariant.LUMO_ERROR);
            return;
        }

        //TODO: exchange amount in given currency
        double creditAmount = CurrencyExchanger.exchange(debitAccount.getCurrency(), creditAccount.getCurrency(), transactionDTO.getAmount());

        accountOperationService.debit(transactionDTO.getInitiatorAccount(), transactionDTO.getAmount());
        accountOperationService.credit(transactionDTO.getReceiverAccount(), creditAmount);

        transactionService.save(transactionDTO);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }
}
