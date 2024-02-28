package com.nexusbank.service.transaction;

import com.nexusbank.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionFacadeService {

    @Autowired
    TransactionContext transactionContext;

    public void init(TransactionDTO transactionDTO) {
        transactionContext.setContext(transactionDTO.getTransactionType()).init(transactionDTO);
    }
}
