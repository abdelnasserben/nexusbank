package com.nexusbank.service.transaction;

import com.nexusbank.dto.TransactionDTO;
import com.nexusbank.mapper.TransactionMapper;
import com.nexusbank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repository;

    public void save(TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            System.err.println("Transaction can't be null");
            return;
        }
        repository.save(TransactionMapper.INSTANCE.toModel(transactionDTO));
    }

    public List<TransactionDTO> findAll() {

        return repository.findAll().stream()
                .map(TransactionMapper.INSTANCE::toDto)
                .toList();
    }

    public void delete(TransactionDTO transactionDTO) {
        repository.delete(TransactionMapper.INSTANCE.toModel(transactionDTO));
    }
}
